package com.dianrong.common.uniauth.cas.model;

/**.
 * 辅助model,记录每一个session进行cas登陆,验证码相关信息
 * ps. 一个session一个，不用进行多线程的考虑
 * @author wanglin
 */
public class CasLoginCaptchaInfoModel {
	/**.
	 * 失败多少次就显示验证码
	 */
	public static int SHOW_CAPTCHA_FAILED_COUNT = 3;
	
	/**.
	 * 重新登陆不显示验证码的等待毫秒数
	 */
	public static long PER_TIME_MILLS_RELOGIN_NO_CAPTCHA = 10L * 60L * 1000L;
	
	/**.
	 * 已经登陆失败次数
	 */
	private int failCount;
	
	/**.
	 * 上一次登陆失败的时间毫秒数
	 */
	private long lastLoginFailedMilles;
	
	/**.
	 * 构造函数
	 */
	public CasLoginCaptchaInfoModel(){
		this.failCount = 0;
		this.lastLoginFailedMilles = System.currentTimeMillis();
	}
	
	/**.
	 * 判断失败一次之后是否能继续正常登陆(不要验证码)
	 * @return 结果
	 */
	public boolean canLoginWithouCaptchaForFailedOnce(){
		failedCountInc();
		return canLoginWithouCaptcha();
	}
	
	/**.
	 * 失败数增加一次
	 */
	public void failedCountInc(){
		//判断是否已经过期了
		if(this.lastLoginFailedMilles + PER_TIME_MILLS_RELOGIN_NO_CAPTCHA < System.currentTimeMillis()){
			reInit();
		}
		
		this.failCount++;
		//刷新最新的失败时间
		this.lastLoginFailedMilles = System.currentTimeMillis();
	}
	
	/**.
	 * 判断是否能够不用验证码的登陆
	 * @return 是否能够不用验证码验证来登陆
	 */ 
	public boolean canLoginWithouCaptcha(){
		if(this.failCount < SHOW_CAPTCHA_FAILED_COUNT){
			return true;
		}
		
		//判断是否已经过期了
		if(this.lastLoginFailedMilles + PER_TIME_MILLS_RELOGIN_NO_CAPTCHA < System.currentTimeMillis()){
			reInit();
			return true;
		}
		return false;
	}
	
	/**.
	 * 重新初始化数据
	 */
	public void reInit(){
		this.failCount = 0;
		this.lastLoginFailedMilles = System.currentTimeMillis();
	}
}
