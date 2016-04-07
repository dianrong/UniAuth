package com.dianrong.common.uniauth.cas.helper.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**.
 * 单例模式的单线程的线程池
 * 只服务于cas cfg的缓存更新操作
 * @author wanglin
 */
public class SingleThreadPool {
	/**.
	 * 恶汉模式单例
	 */
	public static SingleThreadPool instance = new SingleThreadPool();
	
	/**.
	 * java提供的线程池
	 */
	private ExecutorService threadExecutor;
	
	/**.
	 * 私有构造函数
	 */
	private SingleThreadPool(){
		this.threadExecutor = Executors.newSingleThreadExecutor();
	}
	
	/**.
	 * 加载任务到线程池
	 * @param runnable
	 */
	public void  loadTask(Runnable runnable){
		this.threadExecutor.execute(runnable);
	}
	
	/**.
	 * 停止接受任务
	 */
	public void shutDown(){
		this.threadExecutor.shutdown();
	}
	
	/**.
	 * 尝试停止线程的执行
	 */
	public void shutDownNow(){
		this.threadExecutor.shutdownNow();
	}
}
