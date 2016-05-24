package com.dianrong.common.auth.server.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlSequence;
import com.dianrong.common.auth.server.data.entity.SlSequenceExample;
import com.dianrong.common.auth.server.data.mapperAdapter.SlSequenceMapperAdapter;
import com.dianrong.common.auth.server.util.AuthServerConstant;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**.
 * 主要用于获取表的主键id
 * @author wanglin
 *
 */
@Service
public class SlSequenceService {
	
	/**.
	 * 日志处理对象
	 */
	  private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**.
	 * 为了提高效率，对每个表的id进行缓存处理
	 */
	private static ConcurrentHashMap<String, SequenceRange> sequenceMap = new ConcurrentHashMap<String, SequenceRange>();
	
	/**.
	 * 访问SL$SEQUENCE表的mapper
	 */
	@Autowired
	private SlSequenceMapperAdapter slSequenceMapper;
	
	/**.
	  * 获取表对应的主键id
	  * @param seqName sl$sequence中的表名
	  * @return nextId
	  */
	  public Long assignSequenceNumber(final String seqName) {
		  // 采用默认值
		  return assignSequenceNumber(seqName, AuthServerConstant.SEQUENCE_INCREMENT_NUM_PER_TIME);
	  }
	
	 /**.
	  * 获取表对应的主键id
	  * @param pseqName sl$sequence中的表名
	  * @param  fetchSize 刷新缓存
	  * @return nextId
	  */
	  public Long assignSequenceNumber(final String pseqName, int fetchSize) {
			  if(StringUtil.strIsNullOrEmpty(pseqName)) {
				  throw new IllegalArgumentException("seqName can not be null");
			  }
			  final String seqName = pseqName.trim();
			  SequenceRange range = sequenceMap.get(seqName);
		      if(range == null) {
			      synchronized (sequenceMap) {
					if(range == null) {
						// 新建建一个
						range = new SequenceRange(seqName, fetchSize);
						sequenceMap.put(seqName, range);
					}
				}
		      }
		    return range.getSequenceNumber();
	  }

	  // Sequence generator classes
	  class SequenceRange {
	    private String seqName;
	    private AtomicLong index;
	    private Long maxIndex;
	    // 每次缓存的大小
	    private int fetchSize;
	    private AtomicBoolean available;

	    /**.
	     * 初始化
	     * @param seqName 对应name
	     * @param fetchSize 缓存大小
	     */
	    public SequenceRange(String seqName, int fetchSize) {
	    	if(StringUtil.strIsNullOrEmpty(seqName)) {
	    		throw new IllegalArgumentException("seqName can not be null");
	    	}
	      this.seqName = seqName.trim();
	      if(fetchSize <= 0) {
	    	  // 设置默认值
	    	  fetchSize = AuthServerConstant.SEQUENCE_INCREMENT_NUM_PER_TIME;
	      }
	      this.fetchSize = fetchSize;
	      // 暂时不可用
	      this.available = new AtomicBoolean(false);
	      
	      this.index = new AtomicLong(0);
	      this.maxIndex = -1L;
	      init();
	    }
	    
	    /**.
	     * 初始化数据
	     */
	    private void init(){
	    	// 缓存数据
	    	updateSequenceRange();
	    }

	    public boolean isAvailable() {
	      return this.available.get();
	    }

	    /**.
	     * 获取序号
	     * @return 序号
 	     */
	    public Long getSequenceNumber() {
	    	while(true) {
	    		Long result = getSequenceNumberInner();
	    		if(result != null) {
	    			return result;
	    		}
	    		updateSequenceRange();
	    	}
		 }
	    
	    /**.
	     * 内部实现的获取数据的方法
	     * @return
	     */
	    public Long getSequenceNumberInner() {
	      if (!this.isAvailable()) {
	        return null;
	      }
	      Long result = index.getAndIncrement();
	      // 超过了缓存的范围 需要重新进行刷新
	      if (result.compareTo(maxIndex) > 0) {
		         return null;
	      }
	      return result;
	    }
	    
	    /**.
	     * 刷新缓存
	     */
	    private void updateSequenceRange() {
	    	updateSequenceRange(this.fetchSize);
	    }
	    
	    /**.
	     * 重新获取一批数据来进行缓存
	     * @param fetchSize 缓存的大小
	     */
	    private synchronized void updateSequenceRange(int tfetchSize) {
	        if (index.longValue() > this.maxIndex) {
	        	// 设置
	        	this.available.set(false);
	          // 更新数据库
	          // 1 查询当前对应的表的id
	          SlSequenceExample scondtion = new SlSequenceExample();
	          scondtion.createCriteria().andNameEqualTo(this.seqName);
	          List<SlSequence> infoes =  slSequenceMapper.selectByExample(scondtion);
	          // 存储此次最大的序号
	          long currentMaxId = -1L;
	          if(infoes == null || infoes.isEmpty()) {
	        	  // 生成一条
	        	  try {
	        		  currentMaxId = 0L + tfetchSize;
	        		  SlSequence newRecord = new SlSequence();
	        		  newRecord.setName(this.seqName);
	        		  newRecord.setCnt(new BigDecimal(currentMaxId));
  	        		 int insertNum =  slSequenceMapper.insert(newRecord);
	        		// 插入失败了
	        		if(insertNum != 1) {
	        			return;
	        		}
	        	  } catch(Exception ex) {
	        		  // 有可能会出现同时添加出错
	        		  logger.info("sl$sequence insert new record failed", ex);
	        		  return;
	        	  }
	          } else if(infoes.size() == 1) {
	        	  SlSequence currentSl = infoes.get(0);
	        	  currentMaxId = currentSl.getCnt().longValue() + tfetchSize;
	        	  // 直接更新数据
	        	  SlSequence updateRecord = new SlSequence();
	        	  // name
	        	  updateRecord.setName(currentSl.getName());
	        	  
	        	  // 更新后的值
	        	  updateRecord.setCnt(new BigDecimal(currentMaxId));
	        	  
	        	  // 未更新时 cnt的值
	        	  updateRecord.setTempCnt(currentSl.getCnt());
	        	  // 木有成功
	        	  int updateNum = slSequenceMapper.updateCntByNameAndCnt(updateRecord);
	        	  if(updateNum < 1) {
	        		  return;
	        	  }
	          }  else {
	        	  throw new RuntimeException("mutile records name is " + this.seqName);
	          }
	          //update new value
	          this.index.set(currentMaxId - tfetchSize + 1);
	          this.maxIndex = currentMaxId;
	          
	          // 通知可用了
	          this.available.set(true);
	        }
	    }
	  }  
}
