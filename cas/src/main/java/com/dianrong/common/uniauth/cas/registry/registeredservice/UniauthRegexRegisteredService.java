package com.dianrong.common.uniauth.cas.registry.registeredservice;

import com.dianrong.common.uniauth.cas.util.ServiceUtils;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegexRegisteredService;
import org.jasig.cas.services.RegisteredService;

/**
 * Uniauth实现的RegexRegisteredService.
 * <p>
 * 对RegexRegisteredService中的某些实现进行自定义
 * </p>
 *
 * @author wanglin
 */
public class UniauthRegexRegisteredService extends RegexRegisteredService {

  private static final long serialVersionUID = -264961074092372156L;

  /**
   * 用于实现matches和compareTo.
   */
  private boolean serviceIdIsDefault = false;

  @Override
  public boolean matches(final Service service) {
    if (service == null || service.getId() == null) {
      return false;
    }
    if (serviceIdIsDefault) {
      return super.matches(service);
    }
    String regularServiceString = ServiceUtils.getRegularServiceUrl(service.getId());
    return regularServiceString.equals(getServiceId());
  }

  /**
   * 加入serviceIdIsDefault作为排序因子.
   * <p>
   * serviceIdIsDefault=true的排在serviceIdIsDefault=false的前边
   * </p>
   */
  @Override
  public int compareTo(final RegisteredService other) {
    if (other instanceof UniauthRegexRegisteredService) {
      UniauthRegexRegisteredService otherRegisteredService = (UniauthRegexRegisteredService) other;
      if (otherRegisteredService.serviceIdIsDefault && !this.serviceIdIsDefault) {
        return -1;
      }
      if (!otherRegisteredService.serviceIdIsDefault && this.serviceIdIsDefault) {
        return 1;
      }
    }
    return super.compareTo(other);
  }

  public void setServiceIdIsDefault(boolean serviceIdIsDefault) {
    this.serviceIdIsDefault = serviceIdIsDefault;
  }
}
