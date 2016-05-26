(function() {
  var captchaImg = document.getElementById('captcha-img');

  var refreshCaptchaImage = function() {
    var postfix = new Date().getTime();
    
    captchaImg.setAttribute('src', '../images/captcha.jpg?v=' + postfix);
  }

  var captchaRefreshBtn = document.getElementById('captcha-refresh-btn');
  if (captchaRefreshBtn) {
    captchaRefreshBtn.addEventListener('click', function(evt) {
      evt.preventDefault();
      evt.stopPropagation();

      refreshCaptchaImage();
    });
  }


  var identityElem = document.getElementById('identity');
  var passwordElem = document.getElementById('password');
  var captchaElem = document.getElementById('captcha');
  var rememberElem = document.getElementById('remember');
  var loginBtn = document.getElementById('login-btn');

  var enableLoginBtn = function() {
    loginBtn.removeAttribute('disabled');
  }

  var disableLoginBtn = function() {
    loginBtn.setAttribute('disabled', 'disabled');
  }

  var validateForm = function() {
    var identity = identityElem.value;
    var password = passwordElem.value;
    
    if(identity == '' || password == '') {
      disableLoginBtn();
      return;
    }
    
    if(captchaElem && captchaElem.value == '') {
      disableLoginBtn();
      return;
    }

    enableLoginBtn();
  }


  function getRedirectUrl() {
    var redirectUrl = '/account/my-account?fromLogin=true';
    try {
      if (SL_NEXT_URL) {
        redirectUrl = SL_NEXT_URL;
      }
    } catch (e) {
    }

    //if (urlParams.fromUrl) {
    //redirectUrl = decodeURIComponent(urlParams.fromUrl);
    //}
    return redirectUrl;
  }

      
  identityElem.addEventListener('keyup', validateForm);
  passwordElem.addEventListener('keyup', validateForm);
  if (captchaElem) {
    captchaElem.addEventListener('keyup', validateForm);
    captchaElem.addEventListener('blur', validateForm);
  }
  identityElem.addEventListener('blur', validateForm);
  passwordElem.addEventListener('blur', validateForm);

  var login = function() {
    disableLoginBtn();

    if(rememberElem.checked) {
      localStorage.setItem('dr.identity', identity);
    } else {
      localStorage.removeItem('dr.identity');
    }
    document.loginForm.submit();
  }

  loginBtn.addEventListener('click', function(evt) {
    evt.preventDefault();
    evt.stopPropagation();
    
    login();
  });

  var drIdentity = localStorage.getItem('dr.identity');
  if(drIdentity) {
    rememberElem.checked = true;
    identityElem.value = drIdentity;
  } else {
    rememberElem.checked = false;
  }
})();

