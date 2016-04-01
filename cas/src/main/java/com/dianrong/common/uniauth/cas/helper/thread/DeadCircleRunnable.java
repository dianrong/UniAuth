package com.dianrong.common.uniauth.cas.helper.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * 死循环的线程.
 * @author wanglin
 *
 */
public abstract class DeadCircleRunnable implements Runnable{
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * 标志位,表示当前线程是否继续死循环执行
	 */
	private volatile boolean deadCircleRunning;
	
	public DeadCircleRunnable(){
		this.deadCircleRunning = true;
		//加入管理器管理
		DeadCircleThreadManager.addTheadToList(this);
	}
	
	@Override
	public void run() {
		while(this.deadCircleRunning){
			try {
				//业务方法
				execut();
			} catch(Exception ex){
				//异常全部吃掉
				logger.warn(this.getClass().getSimpleName()+" thread is exception", ex);
			} finally {
				//判断是否跳出循环
				if(!this.deadCircleRunning){
					break;
				}
				//休眠
				try {
					Thread.sleep(sleepMilles());
				} catch (InterruptedException e) {
					logger.warn(this.getClass().getSimpleName()+" thread is exception", e);
				}
			}
		}
	}
	
	/**.
	 * 退出死循环
	 */
	public void existDeadCircle(){
		this.deadCircleRunning = false;
		//剔除列表
		DeadCircleThreadManager.removeTheadFromList(this);
	}
	
	/**.
	 * 线程执行的业务方法
	 */
	protected abstract void execut();
	
	/**.
	 * 死循环休眠的毫秒数
	 * @return 毫秒数
	 */
	protected abstract long sleepMilles();

}
