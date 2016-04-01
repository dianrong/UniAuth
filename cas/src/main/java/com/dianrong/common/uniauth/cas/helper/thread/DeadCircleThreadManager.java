package com.dianrong.common.uniauth.cas.helper.thread;

import java.util.HashSet;
import java.util.Set;

/**.
 * 死循环线程的管理类.
 * @author wanglin
 *
 */
public class DeadCircleThreadManager {
	/**.
	 * 管理的所有死循环的线程的引用列表
	 */
	private static Set<DeadCircleRunnable> mamangeThread = new HashSet<DeadCircleRunnable>();
	
	/**.
	 * 添加新的线程进入管理列表
	 */
	public static void addTheadToList(DeadCircleRunnable newThread){
		if(newThread == null){
			throw new NullPointerException();
		}
		mamangeThread.add(newThread);
	}
	
	/**.
	 * 将目标线程从管理列表中剔除
	 */
	public static void removeTheadFromList(DeadCircleRunnable thread){
		if(thread == null){
			throw new NullPointerException();
		}
		mamangeThread.remove(thread);
	}
	
	/**.
	 * 停止所有的线程
	 */
	public static synchronized void stopAllThread(){
		//防止强迭代器异常
		Set<DeadCircleRunnable> tlist = new HashSet<DeadCircleRunnable>(mamangeThread);
		for(DeadCircleRunnable td: tlist){
			td.existDeadCircle();
		}
		//释放
		tlist = null;
	}
}
