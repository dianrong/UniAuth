package com.dr.cas.request;

/**
 * Keys to use when setting/getting request errors from the RequestErrorObject
 * 
 * @author Tom
 * 
 */
public enum RequestErrorKey {

	INVALID_CHARS_LIST("<, >"), 
    UNEXPECTED_ERROR ("An unexpected error has occurred"),
    JSON_BUILD_ERROR ("An unexpected error has occurred"),
    MISSING_PRECONDITION ("An unexpected error has occurred"),
    MISSING_SESSION_DATA ("Some required data was not found, possibly due to session timeout. Please re-enter your information and try again."),
    
    EMAIL_EMPTY ("The email address is required"),
    EMAIL_TOO_LONG("Your email address must be 50 characters or less."),
    INVALID_EMAIL ("Please specify a valid email address"),
    EMAIL_NOT_REGISTER("Your email adress is not registered"),
    INVALID_PASSWORD("Please enter a valid password"),
    INVALID_ACTOR_ID("Invalid actor Id."),
	EMAIL_TAKEN ("Your email address is already registered. Please sign-in or provide a different email address."),
	EMAIL_TAKEN_FOR_LENDER ("This email address is already registered. To open a new account, please provide an alternative email address to the current one on file."),
	DUPLICATE_EMAIL ("Please select a different email address."),
	EMAIL_CONFIRM_EMPTY ("The email confirmation is required"),
	EMAIL_CONFIRM_NOT_MATCH("Email confirmation does not match"),
	PASSWORD_EMPTY ("A password is required"),
	PASSWORD_CONFIRM_EMPTY ("Password confirmation is required"),
	PASSWORD_NO_MATCH ("Password confirmation does not match"),
    PASSWORD_NOT_VALID("Password must have 8 or more characters, with at least 1 number and 1 letter"),
    PASSWORD_TOO_LONG("Your password must be between 8 and 40 characters"),
    PASSWORD_TOO_SHORT("Your password must be between 8 and 40 charachters"),
    PASSWORD_ALREADY_SET("Password has already set"),
    //error related to profile, change password
    OLD_PASSWORD_EMPTY("Your old password is required"),
    OLD_PASSWORD_INCORRECT("Your old password is not correct"),
    NEW_PASSWORD_EMPTY("A new password is required"),
    NEW_PASSWORD_NOT_VALID("The new password must have 8 or more characters, with at least 1 number and 1 letter"),
    NEW_PASSWORD_TOO_LONG("The new password must be between 8 and 40 characters"),
    NEW_PAYMENT_PASSWORD_LENGHT_WRONG("The new password must have 6 characters"),
    PASSWORD_WITH_DUPLICATED_CHARACTERS("The password shouldn't contain duplicated characters"),
    PAYMENT_PASSWORD_NOT_VALID("Password must have 6 characters, with at least 1 number and 1 letter"),
    NEW_PASSWORD_COMFIRM_EMPTY("The new password confirmation is required"),
    //Error related to payment password
    PAYMENT_PASSWORD_NOT_ENTERED("No password was entered, use set password first"),
    PAYMENT_OLD_PASS_OR_NEW_PASS_NOT_ENTERED("New password or old password is missing"),
    NO_PAYMENT_PASSWORD("No password was entered"),
    NOT_INPUT_PAYMENT_PASSWORD("Don't input payment password"),
    PAYMENT_PASSWORD_ALREADY_ENTERED("Payment password already entered, use update password instead"),
    SECURITY_QUESTION_EMPTY ("A security question is required"), SECURITY_QUESTION_INVALID ("An invalid security question was specified"),
	SECURITY_ANSWER_EMPTY ("Enter an answer to the security question"), 
	FIRST_NAME_EMPTY ("Your first name is required"),
	MULTI_FIRSTNAME("It appears that you have entered more than one individual's first name. Please apply using only one person's information."),
	LAST_NAME_EMPTY ("Your last name is required"),
	ACCOUNT_TYPE_EMPTY("Please choose an Account Type"),
	FIRST_NAME_TOO_LONG ("First names are limited to 40 characters"),
	LAST_NAME_TOO_LONG ("Last names are limited to 40 characters"),
	FULL_NAME_TOO_LONG ("Full name is limited to 40 characters"),
	FULL_NAME_EMPTY("Your full name is required"),
	SPOUSE_NAME_EMPTY("请输入配偶姓名"),
	SPOUSE_NAME_TOO_LONG ("配偶姓名长度太长"),
	COUNTRY_CODE_TOO_LONG ("Country code is limited to 2 characters"),
	NAMES_NOT_EDITABLE ("Your identity has already been verified; your name cannot be changed at this time"),
	SCREEN_NAME_EMPTY ("A screen name is required"), 
	SCREEN_NAME_UNAVAILABLE ("Somebody beat you to it: this screen name is already taken"),
	SCREEN_NAME_TOO_LONG ("The screen name must be less than 24 characters long"),
	SCREEN_NAME_TOO_SHORT ("The screen name must be longer than 3 characters"),
	INVALID_SCREENNAME ("The screen name can contain only letters, numbers, dashes, underscores, and dots"),
	INVALID_REFERRER ("The referrer can contain only letters, numbers, dashes, underscores, and dots"),
	INVALID_CREDENTIALS ("You have entered an invalid email address or password"), 
	INVALID_EMAIL_OR_CELLPHONE_NOT_VERIFYED("You have entered an invalid email address or cellphone, cellphone is not verifyed"),
	USER_DOES_NOT_EXIST ("You have entered an invalid confirmation code."), // I think this is intentional...? 
	INVALID_CONF_CODE ("You have entered an invalid confirmation code."),
	BAD_PASSWORD ("You have entered an invalid username or password"),
    MISSING_CAPTCHA ("For further validation, please enter the characters in the image"),
    INCORRECT_CAPTCHA ("Incorrect code. Enter the characters in the image again"),
    UNABLE_TO_SEND_CONFIRMATION_CODE ("An error occurred while sending the confirmation code"),
    SECURITY_CHECK_FAILED ("Your security answer does not match our records.  Please try again"),
    IDENTITY_INFO_MISSMATCH ("The information submitted does not match our records, please contact customer support or try again"),
    PASSWORD_INCORRECT ("You have entered an incorrect password"), // for use only when resetting
    IDENTITY_NOT_VERIFIED ("Your identity has not been verified, please contact customer support or restart the registration process"), 
    MUST_SELECT_INTENT ("You must choose to become either an investor or a borrower"),
    NOT_VALID_INTENT ("The intent is not valid"),
    NOT_VALID_SALE_LINK ("sale link error"),
    NOT_VALID_SALE_NAME ("sale's name error"),
    NOT_VALID_THIRDPARTY_KEY("This third party key is not valid"),

    EMAIL_OR_CELLPHONE_INVALID("both email/cellphone are invalid"),
    
    EMAIL_NOT_FOUND ("We're sorry, but we don't have that email address in our system"),
    MUST_ACCEPT_TOA ("You must accept the terms of use"),
    
    MISSING_PASSWORD_RESET_CODE("Password reset code is empty"),
    PASSWORD_RESET_CODE("Password reset code is incorrect or expired"),
    PASSWORD_RESET_FROZEN("You need to wait before trying again"),
    PASSWORD_RESET_NO_CHALLENGE("There is no challenge"),
    
    SUBSCRIBE_EMAIL_REGISTERED("Your email is already subscribed"),
    
    INVALID_REGISTRATION_STEP("You must complete the previous steps first"),
    
	// errors related to lender registration
    LENDER_REGISTRATION_START_OVER ("We're sorry, something unexpected happened during your registration. Please start over"),
	INVALID_RISK_LEVEL ("You have selected an invalid risk level.  Try again"),
	MUST_ACCEPT_LENDER_AGREEMENTS ("You must accept the Note Purchase agreement"),
	MUST_ACCEPT_FINANCIAL_SUITABILITY ("You must accept the Financial Suitability agreement"),
	MUST_ACCEPT_DECLARATION_OF_TRUST ("You must accept the Declaration of Trust agreement"),
	MUST_ACCEPT_STATE_AND_FINANCIAL_SUITABILITY ("You must accept the State and Financial Suitability agreement"),
	MUST_SATISFY_STATE_CONDITION ("You must satisfy the State Condition"),
	MUST_ACCEPT_BANK_ACCOUNT_VERIFICATION ("You must accept the terms for bank account verification"),
	MUST_ACCEPT_TAX_WITHHOLDING ("You must accept the no tax withholding agreement"),
    LENDER_REGISTRATION_INCOMPLETE ("Lender registration is incomplete"),
    LENDER_REGISTRATION_FULLSSN("We are not able to verify your identity based on the information you provided. Please enter your social security number."),
    LENDER_REGISTRATION_INVALID_ACCOUNT_TYPE("You must provide a valid account type"),
    
	// errors related to agent registration
    AGENT_REGISTRATION_START_OVER ("We're sorry, something unexpected happened during your registration. Please start over"),
	MUST_ACCEPT_AGENTS_AGREEMENTS ("You must accept the Agent Agreement"),
    AGENT_REGISTRATION_INCOMPLETE ("Agent registration is incomplete"),   
    UNKNOWN_BROKER_ID ("We're sorry, the broker ID specified is not recognized, please check with your broker."),
    EMPTY_EIN ("You must submit an EIN unless you use SSN to become an agent"),
    EMPTY_COMPANY_NAME ("You must submit your Company Name unless you use SSN to become an agent"),
	MUST_ACCEPT_INDEPENDENT_BROKER_AGREEMENT ("You must accept Independent Broker Agreement"),
	// errors related with updates
	USER_NAME_CAN_NOT_BE_UPDATED("User name can't be updated. Changing user name is allowed only once"),
	ID_CARD_CAN_NOT_BE_UPDATED("ID can't be changed. Changing ID Card is allowed only once"),
	REAL_NAME_CAN_NOT_BE_UPDATED("Real name can't be updated. Changing real name is allowed only once"),	    
    // errors related to borrower registration
    BORROWER_REGISTRATION_START_OVER ("We're sorry, something unexpected happened during your registration. Please start over"),
    MISSING_LOAN_AMOUNT ("A valid loan amount is required"), 
    INVALID_LOAN_AMOUNT ("Please enter a valid loan amount"),
    LOAN_AMOUNT_MINIMUM ("The loan amount you have entered is below the minimum loan amount allowed"),
    LOAN_AMOUNT_MULTIPLE ("The loan amount must be a multiple of ¥100"),
    MISSING_LOAN_LENGTH ("Loan length is required"), INVALID_LOAN_LENGTH ("Invalid loan length"),
    MISSING_LOAN_TITLE ("A loan title is required"),MISSING_ACTOR_ID ("An actor id is required"),
    LOAN_TITLE_TOO_SHORT ("The loan title must be at least two characters long"),
    LOAN_TITLE_TOO_LONG ("The maximum number of loan title must less than 40 characters."),
    MISSING_LOAN_PURPOSE ("A loan purpose is required"), INVALID_LOAN_PURPOSE ("Invalid loan purpose specified"),
    MISSING_LOAN_DESCRIPTION ("A loan description is required"), LOAN_DESCRIPTION_TOO_LONG ("The loan description must be less than 120 characters"),
    MISSING_LOAN_TYPE ("A loan type is required"), INVALID_LOAN_TYPE ("Invalid loan type specified"),
    MISSING_LOAN_SUBTYPE ("A loan subtype is required"), INVALID_LOAN_SUBTYPE ("Invalid loan subtype specified"),
    MISSING_TOTAL_INCOME ("Your income is required"), INVALID_TOTAL_INCOME ("Invalid income"),
    INVALID_COMPANY_TOTAL_INCOME("Invalid income"),INVALID_COMPANY_TOTAL_COST("Invalid cost"),
    INVALID_COMPANY_OTHER_INCOME("Invalid company other income"),
    MAX_TOTAL_INCOME ("The income you have entered is above the maximum income allowed"),
    MUST_ACCEPT_INCOME_VERIFICATION ("You must accept income verification"),
    MISSING_LOAN_TERMS ("Loan terms are required"),
    INVALID_LOAN_TERMS ("Invalid loan terms"),
    INVALID_JOB_TENURE ("Invalid job tenure"),
    INVALID_EXTERNAL_ID("Invalid external Id."),
    MISSING_JOB_TENURE ("Please enter your job tenure"),
    INVALID_JOB_TENURE_MONTHS ("Invalid job tenure months, please update your job tenure years instead of using more than 11 months"), 
    INVALID_JOB_TENURE_YEARS ("Invalid job tenure"),
    ACTOR_ALREADY_HAS_LOAN ("You already borrowed! You can only have one active loan at a time... for now"),
    LOAN_NOT_APPROVED ("Loan was not approved"),
    INVALID_COLLEGE_YEAR ("Invalid college year"),
    BORROWER_REGISTRATION_INCOMPLETE ("Your borrower registration is incomplete.  Please finish your application"),
    MUST_AUTHORIZE_CREDIT_PROFILE ("You must accept the credit profile authorization"),
    MUST_ACCEPT_CREDIT_SCORE_NOTICE("You must accept the credit score notice"),
    MUST_ACCEPT_BORROWER_AGREEMENT ("You must accept the borrower agreement"),
    MUST_ACCEPT_LOAN_AGREEMENT ("You must accept the loan agreement"),
    MUST_ACCEPT_NC_AGREEMENT ("You must accept the North Carolina Agreement"),
    MUST_ACCEPT_MA_AGREEMENT ("You must accept the Massachusetts Agreement"),    
    MUST_CONFIRM_ADDRESS ("You must confirm that your address information is correct"),
	INVALID_LOAN_AMOUNT_OR_MUST_ACCEPT_AGREEMENTS ("You must enter a valid amount between $1,000 to $35,000 and check each authorization before seeing your offer."),
	MISSING_LOAN_MATURITY ("A loan maturity is required"),
	INVALID_LOAN_MATURITY ("Invalid loan maturity"),
	MISSING_LOAN_PAYMENT_METHOD ("A loan payment method is required"),
    INVALID_LOAN_PAYMENT_METHOD ("Invalid loan payment method"),
    INVALID_PERSONAL_LOAN_PAYMENT_METHOD ("Personal loan payment method must be Amortization!"),
	
	// Errors related to borrower document submission
	UPLOAD_DOCUMENT_ERROR("An error occurred while uploading the document '{documentName}'"),
	UPLOAD_DOCUMENT_EMPTY("The document '{documentName}' is empty"),
	UPLOAD_DOCUMENT_INVALID_TYPE("The document '{documentName}' does not have a valid format"),
	UPLOAD_DOCUMENT_ALREADY_VERIFIED("The document '{documentName}' was already submitted and verified"),
	UPLOAD_DOCUMENT_TOO_MANY_VERSIONS("The document '{documentName}' was submitted too many times"),
	UPLOAD_DOCUMENT_NOT_REQUESTED("The document '{documentName}' was not requested"),
	UPLOAD_DOCUMENT_INVALID_FORMAT("Please only upload the following file formats: JPG, GIF, PDF, PNG and TIFF; None of your files have been uploaded."),
	UPLOAD_DOCUMENT_INVALID_FORMAT_EXCEL("Please only upload the following file formats: PDF, Excel, JPG, GIF, PNG and TIFF; None of your files have been uploaded."),
    UPLOAD_DOCUMENT_EXCEEDED_SIZE("Please only upload files smaller than 8MB; None of your files have been uploaded"),
    
    // Invalid character symbols
    INVALID_CHARS_ERROR_EMPLOYER_CITY("The Employer City may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_LOAN_TITLE("The Loan Title may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_BANK_NAME("The name of the Financial Institution may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_FINANCE_FIRST_NAME("The Account Holder First Name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_FINANCE_LAST_NAME("The Account Holder Last Name may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_FULL_NAME("The Full Name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_FIRST_NAME("The First Name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_LAST_NAME("The Last Name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_HOME_ADDRESS("Street address may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_HOME_CITY("City may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_LOAN_PURPOSE("Loan purpose description may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_JOB_TITLE("Position may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_EMPLOYER("Employer may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_EMPLOYER_ADDRESS("Work address may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_EMPLOYER_WEB_SITE("Employer Website description may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_COMPANY_NAME("Company name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_COUNTRY("Country name may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_PASSWORD("Password may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_NEW_PASSWORD("The new password may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_LOAN_DESCRIPTION("The Loan Description may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_LOAN_ANSWER("The answer may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_INITIAL("The initial may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_DRIVERS_LICENSE("The drivers license may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_SSN_EIN("The SSN or Trust EIN may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_QUESTION("The Question may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_REFERRER("The referrer may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_TRANSFER_AMOUNT("The Transfer Amount may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_PORTFOLIO_NAME("The portfolio name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_PORTFOLIO_DISCRIPTION("The portfolio discription may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_PASSWORD_RESET_CODE("The Password reset code may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_EMAIL("The Email may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_SSN_SHORT("Last 4 digits of your Social Security Number (SSN) may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_ZIP_CODE("zip code may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_BLACKLIST_VALUE("Value may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_BLACKLIST_REASON("Reason may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_WECHAT("wechat may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_REG_CODE("reg code may not have one or more of the following characters: <, >, =, --"),
    INVALID_REG_CODE_TOO_LONG("reg code is limited to 250 characters"),
    INVALID_CHARS_ERROR_NUMBER_CODE("number code may not have one or more of the following characters: <, >, =, --"),
    INVALID_NUMBER_CODE_TOO_LONG("number code is limited to 250 characters"),
    INVALID_CHARS_ERROR_OPERATION_ADDRESS("operation address may not have one or more of the following characters: <, >, =, --"),

    INVALID_CHARS_ERROR_OTHER_ASSET("other assets may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_PERMANENT_AREA("permanent area may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_PERMANENT_ADDRESS("permanent address may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_CHARS_ERROR_CONTACT_NAME("Contact name may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_CONTACT_JOB_TITLE("Contact job title may not have one or more of the following characters: <, >, =, --"),

    INVALID_CHARS_ERROR_REG_CHANNEL("Registration channel may not have one or more of the following characters: <, >, =, --"),
    
    INVALID_SENSITIVE_CONTENT("It appears that you included information which might reveal your identity to potential investors.  Please revise your entry to ensure that it doesn't include your name, Social Security Number, email address, or other information that could identify where you live."),
    INVALID_SENSITIVE_CONTENT_EMPLOYER_NAME("It appears that your Employer name included information which might reveal your identity to potential investors.  Please revise your entry to ensure that it doesn't include your name, Social Security Number, email address, or other information that could identify where you live."),
    INVALID_SENSITIVE_CONTENT_ADDR_CITY("It appears that your city included information which might reveal your identity to potential investors.  Please revise your entry to ensure that it doesn't include your name, Social Security Number, email address, or other information that could identify where you live."),
    INVALID_MERCHANT_SERIAL_NUMBER("You have just typed a invalid merchant serial number, please have a check."),

    
    VEHICLE_TYPE_EMPTY ("The type of vehicle is required"),
    VEHICLE_YEAR_EMPTY ("The model year of the vehicle is required"),
    VEHICLE_MAKE_EMPTY ("The make of the vehicle is required"),
    VEHICLE_MODEL_EMPTY ("The model of the vehicle is required"),
    VEHICLE_ESTIMATED_DOWN_PAYMENT_EMPTY ("The estimated down payment of the vehicle is required"),
    INVALID_VEHICLE_ESTIMATED_DOWN_PAYMENT ("Invalid estimated down payment of the vehicle"),
    VEHICLE_CHOSEN ("You must choose if you have selected your vehicle or not"),
    
    DIRECT_MAIL_CODE_INVALID ("Sorry, the funding code you entered does not match our records"),
    DIRECT_MAIL_CODE_FROZEN("Unfortunately, we didn't match your code. Please contact Customer Support at (888)596-3158 for further assistance."),
    DIRECT_MAIL_CODE_USED ("Sorry, the funding code you entered has already been used towards a loan, please log in and use the email and password you have already given us. You may also contact our Customer Support at (888)596-3158"),
    
    
	// errors related to lender/borrower registration
	MISSING_DOB ("A valid date of birth is required"), INVALID_DOB ("A valid date of birth is required"), UNDERAGE_USER ("You must be at least 18 to use this site"),
	MISSING_STREET_ADDRESS ("A valid street address is required"),
	STREET_ADDRESS_TOO_LONG("Your street address must be 50 characters or less."),
	STREET_CANT_BE_POBOX("P.O. Boxes are not permitted. Please use your physical home address."),
	INVALID_PROVINCE ("Invalid province"),
	MISSING_CITY ("A valid city is required"),
	INVALID_CITY ("Invalid city"),
	INVALID_AREA ("Invalid area"),
	INVALID_WECHAT_ACCOUNT ("Invalid city"),
	INVALID_ACTORID("Please specify a valid actor ID."),
	CITY_TOO_LONG("Your city must be 50 characters or less."),
	MISSING_STATE ("A valid state is required"), INVALID_STATE ("A valid state is required"),
	MISSING_ZIP ("A valid zip code is required"), INVALID_ZIP ("A valid zip code is required"),
	MISSING_HOME_PHONE ("A valid telephone number is required"), INVALID_HOME_PHONE ("A valid homephone number is required"),
	MISSING_CELL_PHONE ("A valid cell phone number is required"), INVALID_CELL_PHONE ("Please enter a valid cellphone in the following format \"13**** or 15******* or 18*****\""), 
	UNSUPPORT_CELL_PHONE("Don't support the phone number which start with 170"),
	INVALID_SSN_NUMBER("Certificate number wrong"),
	INVALID_CELLPHONE_RESPONSE_CODE("Please enter a valid cellphone response code"),DUPLICATE_CELLPHONE("this cellphone already verified"),INVALID_CELLPHONE_REQUEST("Please resend later."),
	
	PRESS_SEND_RESPONSE_CODE("Please press send response code button"),
	CELLPHONE_ALREADY_VERIFIED("this cellphone already verified"),VERIFIED_CELLPHONE_CAN_NOT_CHANGE("can not change saved cellphone"),
	CELLPHONE_ALREADY_REGISTER("this cellphone already register"),
	INVALID_EMAIL_RESPONSE_CODE("Please enter a valid email response code"),
	INVALID_EMAIL_CANCEL_RESPONSE_CODE("Please enter a valid email cancel response code"),
	
	INVALID_RESET_PASSWORD_REQUEST("Password reset code already sent"),
	
	MISSING_HOME_PHONE_NUMBER ("A valid home phone number is required"), INVALID_HOME_PHONE_NUMBER ("Please enter a valid cellphone"),
	INVALID_ALT_PHONE ("A valid alternative telephone number is required"),
	MISSING_DAYTIME_PHONE ("A valid daytime phone number is required"), INVALID_DAYTIME_PHONE ("A valid daytime phone number is required"), 
	INVALID_EVENING_PHONE ("A valid evening phone number is required"),
	MISSING_SSN ("Your Social Security number is required"), 
	MISSING_SSN_SHORT ("Last 4 digits of your Social Security Number (SSN) are required"),
	INVALID_SSN ("Social Security Number entered is not valid"),
	INVALID_SSN_CREDIT_HISTORY ("Invalid Social Security Number. The number you provided does not match your credit history. Please re-enter or call Customer Service at (888) 596-3157."),
	INVALID_SSN_CREDIT_HISTORY_LOCKED ("Invalid Social Security Number. To protect your identity and prevent fraudulent loan requests, you must contact Customer Service at (888) 596-3157 to complete your loan request."),
	INVALID_SSN_SHORT ("Last 4 digits of Social Security Number (SSN) entered are not valid"),
	INVALID_LOAD_AMOUNT ("Please provide a valid load amount"),
	INVALID_FEES("A valid fees number is required"),
	INVALID_MAXIMUM_AMOUNT("A valid maximum amount is required"),
	MISSING_RELATION("Please specify the relation"),
	MISSING_CONTACT_NAME("Contact name is required"),
	CONTACT_NAME_TOO_LONG("Contact name must not be longer than 40 characters"),
	MISSING_CONTACT_PHONE("Contact phone number is required"),
	INVALID_CONTACT_PHONE("The specified contact phone number is invalid"),
	COMPANY_NAME_TOO_LONG("The specified company name must not be longer than 100 characters"),
	// we're not telling people when the SSN is duplicate to prevent releasing too much information
	DUPLICATE_SSN ("An error occurred while submitting your information; please double-check that all fields are correct. If you received this message in error, <a href=\"mailto:support@sinolending.com\">contact us</a>."),
	BORROWER_ACCOUNT_COUNT_EXCEEDED ("We're sorry, but members are currently limited to two loans on Lending Club. Please check back in the future to apply for an additional loan."),
	LENDER_ACCOUNT_COUNT_EXCEEDED ("We're sorry, but members are currently limited to four investor accounts on Lending Club."),
	INSUFFICIENT_PAYMENT_HISTORY ("We're sorry, but we are unable to list your loan at this time because you already have a loan with us, which has less than 6 months of payment history or has not been current at all time over the last 6 months."),
	
    INVALID_ID_VERIFY_ANSWER ("One or more answers were incorrect"),
    
    INVALID_AFFINITY_GROUP ("You have specified an invalid affinity group"),
    
	MISSING_PRIMARY_ACCT_HOLDER_FIRSTNAME ("The first name of the Primary account holder is required"),
	MISSING_ACCOUNT_NAME("The account name is required"),
	MISSING_PRIMARY_ACCT_HOLDER_LASTNAME ("The last name of the Primary account holder is required"),
	PERSONAL_INFO_LENGTH_ERROR_FIRSTNAME ("The length of the first name must be less than 40 characters"),
	PERSONAL_INFO_LENGTH_ERROR_LASTNAME ("The length of the last name must be less than 40 characters"),
	PRIMARY_ACCT_HOLDER_LENGTH_ERROR_FIRSTNAME ("The first name of the Primary account holder must be less than 35 characters"),
	PRIMARY_ACCT_HOLDER_LENGTH_ERROR_LASTNAME ("The last name of the Primary account holder must be less than 35 characters"),
	MISSING_PRIMARY_ACCT_INSTITUTION ("A bank name is required"),
	PRIMARY_ACCT_INSTITUTION_LENGTH_ERROR ("The name of the financial institution must be less than 75 characters"),
	INVALID_PRIMARY_ACCT_TYPE ("Invalid account type"),
	MISSING_PRIMARY_ACCT_TYPE ("An account type is required"),
	MISSING_ROUTING_NUMBER ("A valid routing number is required"), 
	INVALID_ROUTING_NUMBER ("We cannot identify your routing number. Please enter it again; make sure it includes 9 digits without any dashes, spaces or letters."), 
	INVALID_ROUTING_NUMBER_CHECKSUM ("We cannot identify your routing number. Please enter it again; make sure it includes 9 digits without any dashes, spaces or letters."),
	MISSING_ACCOUNT_NUMBER ("A valid bank account number is required"),
	MISSING_NEW_ACCOUNT_NUMBER ("A valid new bank account number is required"),
	MISSING_OLD_ACCOUNT_NUMBER ("A valid old bank account number is required"),
	MISSING_ACCOUNT_TYPE("A valid bank account type is required"),
	MISSING_PERSONAL_INFO_BEFORE_ADD_BANK_ACCT("You must provide your personal info before adding bank account"),
	INVALID_BANK_ACCOUNT_NUMBER ("Please enter your account number using only numbers, without any dashes, spaces or letters"),
	INVALID_BANK_ACCOUNT_NUMBER_ALPHANUMERIC ("Please enter your account number using only numbers, letters and dashes."),
    ACCOUNT_NUMBER_NO_MATCH ("Account number confirmation does not match"),
    PRIOR_ACCOUNT_NUMBER_NO_MATCH ("The account number provided does not match the account currently on file"),
    
    CREDIT_UNVERIFIABLE ("We were unable to verify your identity at this time."),
    CREDIT_UNVERIFIABLE_BORROWER ("We were unable to verify your identity at this time. To protect your identity and prevent fraudulent requests, you must contact customer service at (888) 596-3157 to complete your loan request."),
    CREDIT_UNVERIFIABLE_BORROWER_ASK_SSN ("We need your social security number in order to provide you with a loan rate."),
    CREDIT_SERVICE_FAILURE ("Unable to connect to the credit authorization service"),
	
    // content review, loan description
    DESCRIPTION_ALREADY_MANAGED("One or more loan descriptions have already been managed."),
    
	// errors related to portfolio building
	PORTFOLIO_NO_NOTES_TO_ADD ("Please select notes to add to your order"), 		PORTFOLIO_MISSING_AMOUNT ("A valid amount is required"), 
	PORTFOLIO_ADD_NOTES_UKNOWN_ERROR ("We were unable to add notes to your order at this time, please try again"),
	PORTFOLIO_INVALID_AMOUNT ("A valid amount is required"),
	PORTFOLIO_UNDER_MINIMUM_AMOUNT ("You have entered a value below the minimum amount"), // This is a default value, see package.properties for the i18n key that is used
	PORTFOLIO_AMOUNT_MULTIPLE ("The amount must be a multiple of the specified amount"),
	INVALID_AMOUNT_OF_PORTFOLIOS ("You have specified an invalid amount"),
	INSUFFICIENT_FUNDS ("You do not have enough available funds in your account, you may <a href=/account/addFunds.action>add funds here</a>."), 
	INVALID_PORTFOLIO_AMOUNT_SPECIFIED ("You have specified an invalid amount"),
    PORTFOLIO_MISSING_RISKLEVEL ("A valid risk level is required"),
	PORTFOLIO_SUBMIT_FAILURE ("An error has occurred while submitting your order. Please try again later."),
	MISSING_LOANFRACTION_GUID ("This is an invalid loan or amount"), 
	PORTFOLIO_INVALID_ID ("This is an invalid order or portfolio"),
    INVALID_LOANFRACTION_GUID_SPECIFIED ("Invalid note, please refresh your screen."),
    LOANS_ALEADY_IN_PORTFOLIO("Some of the Notes selected are already in your order"),
    LOANS_ADDED_TO_PROTFOLIO("The selected Notes have been added to your order."),
    UNABLE_TO_REMOVE_LOANFRACTION ("An error has occurred while removing one or several Notes. Please try again later."),
    PORTFOLIO_MISSING ("Please create a portfolio to continue"),
    MUST_QUALIFY ("must_qualify"),
    INVALID_INVESTED_AMOUNT("Invalid invested amount"),
    INVEST_AMOUNT_LESS_THAN_ONE_HUNDRED("Invest amount can't less than 100"),
    MORE_THAN_MAX_AMOUNT("amount can't more than 30000"),
    NOT_XIAOMING_LOAN("it is not xiaoming loan"),
    INSUFFICIENT_LOAN_INVENTORY ("There are not enough Notes available to build a portfolio at this time, please try again later."),
    ORDER_EXISTS ("An open order already exists. Please <a href=/portfolio/viewOrder.action>View order</a> to view/modify it."),
    PORTFOLIO_UNDIVERSIFICATION_WARNING ("This action will cause your order to be undiversified"),
	AMOUNT_SPECIFIED_GREATER_THAN_UNFUNDED_AMOUNT ("The amount you want to invest exceeds the amount left to fund"),
	THE_CONSORTIUM_IS_FULL("The consortium is full"),
	PORTFOLIO_AMOUNT_MORE_THAN_MAXIMUM_ALLOWED("Failed to submit order as the invested amount is more than maximum allowed"),
	PORTFOLIO_AMOUNT_LESS_THAN_MINIMUM_ALLOWED ("The order has less funds in it than the minimum allowed"),
	PORTFOLIO_AMOUNT_MORE_THAN_MAXIMUM_MONTHLY_ALLOWED("The order has more funds in it than the maximum monthly allowed"),
	PORTFOLIO_AMOUNT_MORE_THAN_LOAN_MAXIMUM_ALLOWED("Failed to submit order as the invested amount is more than maximum allowed"),
	MISSING_GUID_FOR_PORTFOLIO_OPERATION ("We're sorry, you need to select a portfolio before performing this action."),
	PORTFOLIO_TITLE_TOO_LONG ("The portfolio title you have specified is too long"),
	PORTFOLIO_TITLE_MISSING ("The portfolio title cannot be blank"),
	INVALID_PORTFOLIO_TITLE ("The portfolio name can contain only letters, numbers, spaces, dashes, underscores, and dots"),
	PORTFOLIO_INVALID_STATE("Residents from your state can only invest in Lending Club Notes via our Note Trading Platform operated by FOLIO<i>fn</i>"),
    MISSING_GUID ("Missing parameter"),
    INVALID_GUID ("Missing parameter"),
    INVALID_AMOUNT ("You have specified an invalid amount"),
    INVALID_AMOUNT_VALUE_TOO_LARGE ("You have specified an amount that is too large"),
    INVALID_AMOUNT_VALUE_TOO_SMALL("You have specified an amount that is too small"),
    PORTFOLIO_OVERFLOW ("This amount is too high"),
    PORTFOLIO_AMOUNT_HIGHER_THAN_AVAILABLE_AMOUNT ("This amount is higher than your current cash balance, please <a href=/account/addFunds.action>add funds</a>."),
    PORTFOLIO_INVALID_BUYER("Can't purchase your own notes"),
    INVALID_LOAN_SPECIFIED ("Invalid loan"),
    LOAN_NOT_LONGER_AVAILABLE("The loan is not longer available for investments"),
    LOAN_NOT_FOUND ("Loan not found"),
    INVALID_PORTFOLIO_SPECIFIED ("Invalid portfolio"),
    ACTOR_ALREADY_HAS_AN_OPEN_PORTFOLIO ("A portfolio is already open"),
    ACTOR_DOES_NOT_EXIST ("Actor does not exist"),
    INCONSISTENT_ORDER ("Due to an inconsistency in your order, it has been removed."),
    INCONSISTENT_ORDER_NO_MARKOPOINTS ("We were unable to create your order, please try again."),
    NOT_AUTHORIZED_TO_BUY("Lender not authorized to buy this note"),
    GROUP_AVAILABLE_ONLY_TO_FIRST_TIME_INVESTORS("This is only available to first time investors."),
    
    // errors related to search/browse
    INVALID_SEARCH_CRITERIA ("Invalid search criteria"),
    INVALID_PAGING_INFO ("Invalid page"),
    
    
    MISSING_NET_WORTH("Please select a value for Net Worth"),
    MISSING_FIELDS ("One or more required fields were missing"),
    INVALID_ACCOUNT ("An invalid account was specified"),
    INVALID_PINCODE ("The amount entered must be greater than or equal to $0.01 and less than or equal to $0.99."),
    INCORRECT_PINCODE ("The verification amount specified was incorrect"),
    ACCOUNT_VERIFICATION_ATTEMPTS_EXCEEDED ("We were unable to verify your account. This account will now be removed. You may add the account again if you wish to start over, or you can add a different account instead."),
    
    
    FACEBOOK_PUBLISH_ERROR ("Unable to add to your Facebook profile at this time"),
    FACEBOOK_PUBLISH_TITLE_LENGTH ("The title you have specified is too long"),
    FACEBOOK_PUBLISH_TITLE_EMPTY ("You must specify a title to publish to Facebook"),
    
    IMAGE_STORE_FILE("Unable to store the profile image"),
    
    ACCOUNT_OVERFUNDING ("You have specified an amount which will lead to account overfunding"),
    //related to contact us form
    EMAIL_SUBJECT_EMPTY ("An email subject is required"), 
    QUESTION_DESCRIPTION_EMPTY ("Content for your question or comment is required"), 
    INVALID_REPORT_A_LOAN_ID("If you are reporting a loan, please identify it by name or borrower screenName"),
    
    // invitation errors
    NO_INVITATION_ID ("We could not find your loan offer"),
    INVALID_INVITATION ("We have encoutered a problem with your invitation, please sign in if you have an account and password, or if you received an email with an invitation please follow that link again."),
    INVALID_INVITATION_ID ("We could not find your loan offer"),
    INVITATION_OFFER_REFUSED ("We are unable to offer you a loan at this time"),
    INVITATION_ID_VERIFICATION_FAILED ("We could not verify your identity with the information you provided."),
    INVITATION_NOT_ACCEPTED("You must first review your loan offer and create a borrower member."),
    
    // Co-signer errors
    INVALID_COSIGNER_OFFER("We could not find your co-signer information"),
    COSIGNER_OFFER_RETRIEVAL_FAILED("We are currently not able to process your Co-signer information"),
    NO_CONSIGNER_INVITATION_ID("We could not find your co-signer information"),
    COSIGNER_SSN_REQUIRED("Please enter the last 4 digits of your Social Security Number"),
    COSIGNER_ZIP_REQUIRED("Please enter a valid zip code"),
    MISSING_RELATIONSHIP("A valid relationship is required"),
    
    //Borrower account summary
    NOTE_TO_LENDER_EMPTY ("You must enter a loan description for investors"),
	NOTE_TO_LENDER_TOO_LONG("Your loan description is too long. Please remove some characters."),
      
    //Borrower question answer
    INVALID_QUESTION_SPECIFIED ("Invalid question"),
    ANSWER_NULL("You cannot submit an empty answer."),
    ANSWER_TOO_LONG("Your answer is too long. Please remove some characters."),
    CONTAIN_SENSITIVE_CHARACTER("Your input contain sensitive character"),
   
    NO_QUESTION_ASKED ("Please choose from the list provided which question you would like to ask."),
    NO_QUESTION_ANSWERED ("Please write your answer to the lender in the space provided"),
    
    // feedback page errors
    NO_FEEDBACK_CATEGORY_CHOSEN ("Please choose a category"),

    // invite friend errors
    IMPORT_CONTACTS_NO_EMAIL_ADDRESS ("Please specify an email account for importing contacts"),
    IMPORT_CONTACTS_NO_PASSWORD ("Please specify your email account password for importing contacts"),
    IMPORT_CONTACTS_LOGIN_FALIED ("We were unable to access your account. Please try again later."),
    IMPORT_CONTACTS_SERVICE_ERROR ("We are unable to retrieve contacts from your account at this time. Please try again later."),
    IMPORT_CONTACTS_UNSUPPORTED_SERVICE ("We do not currently support that service for importing contacts. Please try one of the other services we support."),
    IMPORT_CONTACTS_INVALID_CREDENTIALS ("We were unable to use the information you gave to access your contacts; please check your information and try again"),
    IMPORT_CONTACTS_NO_CONTACTS_IN_FILE ("We were unable to read any uploaded contacts from your file"),
    INVITE_FRIENDS_NO_CONTACTS_SELECTED ("You must specify at least one valid recipient to invite"),
    INVITE_FRIENDS_WRONG_CONTACTS ("One or more of the email addresses you used are not correct, please try again"),
    INVITE_FRIENDS_EMAIL_TOO_LONG ("One or more of the email addresses you used has over 95 characters, please use an alternate email address"),

    INVITE_FRIENDS_NO_MESSAGE ("Please say something in your message"),
    INVITE_UNABLE_TO_OPT_OUT ("We're sorry, we were unable to opt you out of our invitation program. An error has been logged and will be investigated."),
    INVITE_UNABLE_TO_OPT_IN ("We're sorry, we were unable to opt you into our invitation program. An error has been logged and will be investigated."),
    INVITE_OPT_OUT_INVALID_INFO ("The opt-out information you provided is invalid; please check your URL. If you feel you have received this message in error, please contact us at support@sinolending.com to opt out of future Lending Club invitations."),
    INVITE_OPT_IN_INVALID_INFO ("The opt-in information you provided is invalid; please check your URL. If you feel you have received this message in error, please contact us at support@sinolending.com to opt in for future Lending Club invitations."),
    
    INVITE_BONUSES_RETRIEVAL_ERROR ("Invitation bonus information is not available at this time. An error has been logged and will be investigated."),
    INVITE_BONUSES_INVALID_CHARITY_SPECIFIED ("Please select a valid destination for your bonuses"),
    INVITE_BONUSES_SET_CHARITY_ERROR ("We were unable to set your preference at this time. An error has been logged and will be investigated."),
    
    // Uncrunch Promote error messages
    RECIPIENT_UNABLE_TO_OPT_OUT ("We're sorry, we were unable to opt you out of receiving our email messages. An error has been logged and will be investigated."),
    RECIPIENT_UNABLE_TO_OPT_IN ("We're sorry, we were unable to opt you into receiving our email messages. An error has been logged and will be investigated."),
    RECIPIENT_OPT_OUT_INVALID_INFO ("The opt-out information you provided is invalid; please check your URL. If you feel you have received this message in error, please contact us at support@sinolending.com to opt out of future Lending Club emails."),
    RECIPIENT_OPT_IN_INVALID_INFO ("The opt-in information you provided is invalid; please check your URL. If you feel you have received this message in error, please contact us at support@sinolending.com to opt in for future Lending Club emails."),
    SEND_MESSAGE_NO_CONTACTS_SELECTED ("You must specify at least one valid recipient"),
    SEND_MESSAGE_NO_MESSAGE ("Please say something in your message"),
    
    ACCOUNT_LOCKED("If you are having trouble logging into your account please call customer support at (888) 596-3158"),
    ACCOUNT_LOCKED_SUSPICIOUS_MOVEMENTS("SUSPICIOUS_MOVEMENTS"),
    ACCOUNT_LOCKED_UNVERIFIED_IDENTITY("UNVERIFIED_IDENTITY"),
    ACCOUNT_LOCKED_VIOLATION_TERMS_USE("VIOLATION_TERMS_USE"),
    ACCOUNT_LOCKED_USING_SSN_WITH_LOCKED_ACCOUNT("USING_SSN_WITH_LOCKED_ACCOUNT"),
    CONTACT_CUSTOMER_SERVCE("We are unable to process your request.  Please contact customer service"),
    ACCOUNT_FAIL_TIME("Login fail"),
    ACCOUNT_FAIL_TIME_4("Fail 4 times"),
    ACCOUNT_FAIL_TIME_5("Fail 5 times"),
    ACCOUNT_FAIL_TIME_6("Fail 6 times"),
    ACCOUNT_FAIL_TIME_7("Fail 7 times"),
    ACCOUNT_FAIL_TIME_8("Fail 8 times"),
    ACCOUNT_FAIL_TIME_9("Fail 9 times"),
    ACCOUNT_FAIL_TIME_10("Fail 10 times"),
    ACCOUNT_FAIL_TIME_11("Account locked"),
    // ---- Auto Reinvest ----
    INVALID_REINVEST_STATUS ("You have entered an invalid status"),
    INVALID_REINVEST_INTERVAL("You have entered an invalid interval"),
    NO_TRIGGER_FOUND("Please define your investment alert plan criteria to continue"),
    TRIGGER_LIMIT_EXCEEDED("You cannot add more criteria at this time"),
    TRIGGER_INPUT("Please verify the interest rate and amount of your reinvestment plan"),
    TRIGGER_LM_MISMATCH("There are no Notes matching your search criteria at this time, please try again later."),
    TRIGGER_AMOUNT_INVALID_25("You must use a investment alert plan amount that is a multiple of $25"),
    TRIGGER_AMOUNT_INVALID_NEG("You must use a investment alert plan amount that is a positive amount"),
    TRIGGER_AMOUNT_INVALID("You must use a investment alert plan amount that is a multiple of $25 and greater than $0"),
    TRIGGER_RATE_INVALID("Please enter a desired interest rate for your reinvestment plan that is within the valid range"),
    TRIGGER_FREQUENCY_INVALID("Please enter a desired alert frequency for your reinvestment plan"),
    TRIGGER_STATUS_INVALID("Please enable or disable your investment alert plan criteria"),
    TRIGGER_LOAN_AMOUNT_INVALID("Invalid loan amount range"),
    TRIGGER_MATURITY_INVALID("Invalid loan maturity range"),
    TRIGGER_MIN_BALANCE_INVALID("Invalid minimum balance"),
    TRIGGER_PAYMENT_MODE_INVALID("Payment mode is invalid"),
    TRIGGER_INVESTMENT_TYPE_INVALID("Investment type is invalid"),
    TRIGGER_PERCENTAGE_INVALID("Invalid percentage selected"),
    TRIGGER_FORBID("This user is not allowed to update trigger status."),
	// ---- Virtual Lender AutoInvest ----
	VIRTUAL_LENDER_BALANCE_NOT_ENOUGH("virtual lender balance is not enough"),
    
    // ---- Funds Errors ----
	WITHDRAW_FUNDS_RESUBMIT ("Your request to withdraw funds to your bank account has already been submitted."),
	ADD_FUNDS_RESUBMIT ("Your request to transfer funds from your bank account has already been submitted."),
	SCHEDULED_AUTO_LOADS_MAX_EXCEEDED("You cannot schedule any more repeating fund transfers, please delete any existing repeating transfers first."),
	SCHEDULED_AUTO_LOADS_CANNOT_CANCEL("You cannot cancel your scheduled fund transfer at this time"),
	ADD_FUNDS_INVALID_START_DATE("For repeating fund transfers, the earliest start date is tomorrow."),
	INVALID_ADD_FUNDS_IRA("Please contact Lending Club Lender Services at 888-381-9309. You are not authorized to add funds in this manner."),
	INVALID_WITHDRAW_FUNDS_IRA("Please contact Lending Club Lender Services at 888-381-9309. You are not authorized to withdraw funds in this manner."),	
    ARTICLE_ALREADY_SUBMITTED("You have already submitted an article."),
    
    // ---- Uncrunch Listing Errors ----
	INVALID_USERNAME("The User Name can contain only letters, numbers, dashes, underscores, and dots"),
    USER_NAME_UNAVAILABLE ("Somebody beat you to it: this User Name is already taken"),
    USER_NAME_TOO_LONG ("The User Name must be less than 24 characters long"),
    USER_NAME_TOO_SHORT ("The User Name must be longer than 3 characters"),
    USER_NAME_REQUIRED("A User Name is required"),
    NO_STORY_CREATED_TO_EDIT("You have not yet created a story to edit!"),
    NO_AUTHORITY_TO_EDIT("You do not have the authority to edit this story"),
    ARTICLE_TO_EDIT_NONEXISTANT("The story you are trying to edit does not exist!"),
    ERROR_DELETING_ARTICLE("There was an error while deleting your story."),
    TITLE_REQUIRED_TO_SUBMIT("A title is required to submit a story!"),
    DESCRIPTION_REQUIRED_TO_SUBMIT("A description is required to submit a story!"),
    ALREADY_VOTED_FOR_ARTICLE("You have already reviewed this story!"),
    STORY_DOES_NOT_EXIST("The story you are looking for does not exist!"),
    ARTICLE_HAS_BEEN_REPORTED("This story has been reported to Uncrunch.org"),
    ERROR_GETTING_STORY("There was an error. Please try again."),
    TITLE_EXCEEDS_MAX_LIMIT("The title length exceeded the maximum limit allowed (40 characters)"),
    DESCRIPTION_EXCEEDS_MAX_LIMIT("The story length exceeded the maximum limit allowed (10,000 characters)"),
    ERROR_SUBMITTING_ARTICLE("An error has occurred while submitting your story."),
    ONE_ENTRY_PER_USER("You can only submit one story per user."),
    CONTENT_NOT_ENTERED("You must enter valid content for all fields"),
    
    // ---- Customer Feedback ----
    NAME_MISSING("A name is required to submit a feedback!"),
    EMAIL_MISSING("An email address is required to submit a feedback!"),
    CATEGORY_MISSING("A valid category must be chosen to submit a feedback!"),
    SUBTOPIC_MISSING("A valid subtopic must be selected to submit a feedback!"),
    DESCRIPTION_MISSING("A description is required to submit a feedback!"),
    
	// errors related to screen name changing
	DUPLICATE_SCREEN_NAME ("Please select a different screen name."),
	SCREEN_NAME_TAKEN ("This screen name is already registered.  Please provide a different screen name."),
	
    //--------------------------------
    //----- New Borrower Registration
    //--------------------------------
    ENTER_VALID_WEBSITE("Please enter a valid url address"),
    MISSING_JOB_TITLE ("Please enter a valid job title"), 
    JOB_TITLE_TOO_LONG("Your job title must be 40 characters or less."),
    MISSING_CREDIT_SCORE("A credit score is required"),
    INVALID_CREDIT_SCORE("Please enter a valid credit score"),
    MISSING_HOME_STATUS("Please enter your home status"),
    MISSING_HOME_RENT_AMOUNT("Please enter the home rent amount"),
    INVALID_HOME_RENT_AMOUNT("Please enter a valid home rent amount"),
    MISSING_MORTGAGE_AMOUNT("Please enter the mortgage amount"),
    INVALID_MORTGAGE_AMOUNT("Please enter a valid mortgage amount"),
    MAX_HOME_RENT_AMOUNT("The home rent amount you have entered is above the maximum rent amount allowed"),
    MAX_MORTGAGE_AMOUNT("The mortgage amount you have entered is above the maximum mortgage amount allowed"),
    MISSING_WORK_PHONE ("A valid work phone number is required"),
    INVALID_WORK_PHONE ("A valid work phone number is required"),
    MUST_SELECT_ANSWERS ("You must choose one answer for each question"),
    CANNOT_ADD_NOTE_TO_LENDERS("Cannot add note to lenders this time"), 
    INVALID_LOAN_APPLICATION("Invalid loan application"),
    INVALID_ACTOR_ADDRESS("Invalid address"),
    MUST_BE_BORROWER("The user is not a borrower"),
    MUST_BE_LENDER("The user is not a lender"),
    LOAN_ALREADY_ACCEPTED("Loan app already accepted"),
    MISSING_EMPLOYMENT_STATUS("Please enter your employment status"),
    SAME_HOME_WORK_PHONE("Home phone and work phone cannot be the same"),
    MISSING_EMPLOYER("Please enter your employer name"),
    EMPLOYER_TOO_LONG("Your employer name must be 40 characters or less."),
    MISSING_INCOME_INTERVAL("Please select whether your income is monthly or annual"),
    JOB_TENURE_NEGATIVE("Job tenure cannot be negative"),
    INCOME_NEGATIVE("The income you have entered is negative"),
    MISSING_EMPLOYMENT_ZIP ("A valid employment zip code is required"),
    INVALID_EMPLOYMENT_ZIP ("A valid employment zip code is required"),
    MISSING_EMPLOYMENT_STATE ("A valid employment state is required"),
    INVALID_EMPLOYMENT_STATE ("A valid employment state is required"),
    MISSING_EMPLOYMENT_CITY ("A valid employment city is required"),
    EMPLOYMENT_CITY_TOO_LONG("Your employment city must be 40 characters or less."),
    MISSING_EMPLOYMENT_STREET_ADDRESS ("Employment address is required"),
    EMPLOYMENT_STREET_ADDRESS_TOO_LONG("Your employment address must be 40 characters or less."),
    EMPLOYER_WEB_SITE_TOO_LONG("Your employer website must be 40 characters or less."),
    INVALID_EMPLOYMENT ("Please enter valid employment information"),
    MISSING_COMPANY_PHONE ("A company phone is required"),
    INVALID_COMPANY_PHONE ("Invalid company phone extention"),
    INVALID_COMPANY_PHONE_EXT ("Invalid company phone"),
    MISSING_COMPANY_TYPE ("A company type is required"),
    INVALID_COMPANY_TYPE ("Invalid company type"),
    MISSING_COMPANY_SEGMENT ("A company segment is required"),
    INVALID_COMPANY_SEGMENT ("Invalid segment phone"),
    MISSING_COMPANY_SIZE ("A company size is required"),
    INVALID_COMPANY_SIZE ("Invalid company size"),
    MISSING_YEARS_LIVED ("The years lived in the address are required"),
    INVALID_YEARS_LIVED ("Invalid years lived"),
    
    //----- CREDIT call
    INVALID_CREDIT_INCOME("Invalid credit income"),
    INVALID_CREDIT_INPUT("Invalid credit input"),
    PERSONAL_INFO_LOCKED("Cannot change personal info"),
    COSIGNER_PERSONAL_LOCKED("Cannot change co-signer personal info"),
    EMPLOYMENT_INFO_LOCKED("Cannot change employment info"),
    NAME_EMPTY("First name and last name cannot be empty"),
    PHONE_EMPTY("Home phone and work phone cannot be empty"),
    CREDIT_CALL_ERROR("Your credit information"),
    MUST_PROVIDE_ANSWERS("Please provide answers to all questions"),
    
    //------ Statistics erors
    INVALID_DATES_IN_SEARCH_("The selected dates are invalid, please choose a valid period"),
    
    //------ CSV Exporter
    ENTER_VALID_FROM_DATE("Please enter a valid from date."),
    ENTER_VALID_TO_DATE("Please enter a valid to date."),

    
    TEMPORARY_PROBLEM("We had a temporary problem, please try again"),

    UNABLE_TO_ADD_LOANS_TO_PORTFOLIO("We were unable to add loans to your portfolio, please try again"),

    INVALID_PORTFOLIO_SPECIFIED_CANNOT_REMOVE("Invalid portfolio specified, we were unable to remove a non-existent portfolio."),
    INVALID_PORTFOLIO_SPECIFIED_CANNOT_ADD_LOANS("Invalid portfolio specified, we were unable to add the loans you requested to a non-existent portfolio."),
    INVALID_PORTFOLIO_SPECIFIED_CANNOT_RENAME("Invalid portfolio specified, we were unable to rename a non-existent portfolio."),


    UNABLE_TO_RENAME_PORTFOLIO_DUPLICATE_NAME("We were unable to rename your portfolio, the name has already been used"),
    UNABLE_TO_RENAME_PORTFOLIO_INVALID_NAME("We were unable to rename your portfolio, please try a valid name"),
    UNABLE_TO_RENAME_PORTFOLIO_TRY_AGIN("We were unable to rename your portfolio, please try again"),


    UNABLE_TO_UPDATE_PORTFOLIO_INVALID_NAME("We were unable to update your portfolio, please try a valid name"),

    UNABLE_TO_CHANGE_PORTFOLIO_DESCRIPTION_NONEXISTANT("Invalid portfolio specified, we were unable to change the description of a non-existent portfolio."),
    UNABLE_TO_CHANGE_DESCRIPTION_TRY_AGAIN("We were unable to change your portfolio description, please try again"),


    SELECT_PORTFOLIOS_TO_MERGE("Please select the portfolios you wish to merge before proceeding."),
    USER_NOT_AUTHENTIC("User not authenticated"),
    INVALID_PORTFOLIO_RETRIEVE_DETAILS("Invalid portfolio specified, we were unable to retrieve the portfolio details."),
    COMMUNICATION_ERROR("unexpected communication error"),
    ENTER_ALPHANUMERIC_CHAR_PORTFOLIO_NAME("Only alphanumeric space , _  - # and . are allowed for portfolio names"),
    DESCRIPTION_ENTER_ALPHANUMERIC_CHAR("Only alphanumeric space , _  - # and . are allowed for portfolio descriptions. "),
    DUPLICATE_NAME_TRY_AGAIN("The name you chose has already been used, please try again with a different name."),
    FAILED_TO_MERGE_PORTFOLIOS("Failed to merge Portfolios. Please try again"),
    LOAN_SELECTION_INVALID("Your selection is invalid, please verify your loan selection"),
    UNABLE_TO_REMOVE_LOANS("We were unable to remove Notes from your portfolio, please try again"),

    //------------------------------------------
    //----------GLOBAL FILE DOWNLOAD------------
    //------------------------------------------
    FILE_DOWNLOAD_PARAMETER_ERROR("Filename and type cannot be empty"),
    FILE_DOWNLOAD_EXTENSION_ERROR("The file extension cannot be empty"),
    FILE_DOWNLOAD_UNSUPPORTED_EXTENSION_ERROR("Unsupported file extension"),
    FILE_DOWNLOAD_ERROR("File is not available"),
    
    DATA_FILE_GENERATION_ERROR("Unfortunately there was a problem while generating our data report, please try again later"),
    MONTHLY_STATEMENT_GENERATION_ERROR("There was a problem while generating your Monthly report, please try again later"),
    YEARLY_STATEMENT_GENERATION_ERROR("Unfortunately there was a problem while generating your Yearly Statement, please try again later"),
    TAX_STATEMENT_GENERATION_ERROR("Unfortunately there was a problem while generating your Tax Statement, please try again later"),
    
    FAILED_TO_CREATE_PORTFOLIO("Failed to create new portfolio. Please try again"),


    //-------------------------------------------
    // ---- LENDER ACTIVITY/ACCOUNT ACTIVITY ----
    //-------------------------------------------
    ACCOUNT_ACTIVITY_ERROR("There has been an unexpected error retrieving your Account Activity. Please try again."),
    SEARCH_EXCEEDED_MAXIMUM("Your search has exceeded the maximum 1000 results, please adjust your search."),
    NO_ACCOUNT_ACTIVITY("There was no activity during this period"),
    START_DATE_BEFORE_END_DATE("Your start date must come before your end date."),
    SEARCH_PERIOD_LIMIT_LENDER_ACTIVITY("Your search period must be no more than 31 days."),
    USE_END_DATE_AFTER_START_DATE("Please use an end date after your account signup date."),
    END_DATE_BEFORE_TODAY("Please use an end date before today's date"),
    START_DATE_BEFORE_TODAY("Please use a start date before today's date."),
    ENTER_VALID_START_DATE("Please enter a valid start date."),
    ENTER_VALID_END_DATE("Please enter a valid until date."),
    ACTIVITY_OLDER_THAN_6MONTHS_NOT_AVAILABLE("Activity older than 6 months is not available."),
    TRANSFER_AMOUNT_INVALID("For any transfers, the amount entered must be a valid positive number."),

    /**************************************************
        		       SECONDARY MARKETS
     ***************************************************/

    //----------------------------
    //  ---- TRADER AGREEMENT ----
    //----------------------------
    MISSING_COMPANY_NAME("A valid company name is required"), 
    MISSING_ADDRESS("A valid address is required"), 
    MISSING_OTHER_COMPANY_LOAN_STATUS("Please input the loan stauts"),
    MISSING_COUNTRY("A valid country is required"), 

    INVALID_EMPLOYMENT_STATUS("Please select a valid Employment Status"),

    INVALID_EMPLOYMENT_OCCUPATION("Please select a valid Employment Occupation"),
    MISSING_EMPLOYMENT_OCCUPATION("Please select your Employment Occupation"),

    INVALID_ANNUAL_INCOME("Please select a valid Annual Income range"),
    MISSING_ANNUAL_INCOME("Please select your Annual Income range"),

    INVALID_LIQUID_NET_WORTH("Please select a valid Liquid Net worth range"),
    MISSING_LIQUID_NET_WORTH("Please select your Liquid Net worth range"),

    INVALID_TOTAL_NET_WORTH("Please select a valid Total Net worth range"),
    MISSING_TOTAL_NET_WORTH("Please select your Total Net worth range"),

    MUST_ACCEPT_TOTA ("You must accept the terms of Trading Member Agreement to continue"),
    MUST_ACCEPT_TOTD ("You must acknowledge the disclosures to continue"),

    MISSING_FINANCIAL_INFO("Please verify that you have submitted all your Financial Information"),
    MISSING_EMPLOYMENT_INFO("Please verify that you have submitted all your Employment Information"),
    MISSING_CHILDREN_STATUS("A valid children status is required"), 
    MISSING_ASSET_HOUSE("A valid asset house is required"), 
    MISSING_ASSET_CAR("A valid aseet car is required"), 
    AGREEMENTS_REMAINING("Please verify that you have checked all required agreements"),
    TRADER_REGISTRATION_UNKNOWN_ERROR("There was an unknown error with your Trading Member Registration, please try again"),
    TRADER_ALREADY_REGISTERED("You have already registered as a Trading Member.  The information submitted cannot be changed at this time"),    
    TRADER_SUBMIT_ERROR("There was an error processing your application. Please try again."),
    CITIZENSHIP_NOT_SET("Citizenship status has not been set"),

    TRADER_STEP_ONE_INCOMPLETE("You must complete Step 1 of the Trading Member Registration first"),
    TRADER_STEP_TWO_INCOMPLETE("You must complete the previous steps of the Trading Member Registration first"),

    //---------------------------
    //----- TRADER STATEMENT ----
    //---------------------------
    END_DATE_ERROR("Please use an end date before today's date"),

    //----------------------------
    // ---- TRADING INVENTORY ----
    //----------------------------
    NO_LOANS_TO_SELL("There are no Notes that are eligible for listing."),



    //---------------------------------
    // ---- CONFIRM LOANS FOR SALE ----
    //---------------------------------
    UNEXPECTED_ERROR_LOANS_FOR_SALE("There was an unexpected error, please try again"),
    CANNOT_SELL_NOTES("One or more of your Notes cannot be sold."),
    NOTES_ARE_NOT_YOURS("One of the Notes belongs to a different user"),
    INVALID_SELL_PRICE("One of the Notes has an invalid selling price"),
    INVALID_ORDER_EXPIRATION_DATE("Please enter a valid Order Expiration Date."),    
    TOO_LOW_ASKING_PRICE("Asking price(s) is too low. "),    
    TOO_HIGH_ASKING_PRICE("Asking price(s) is too high. "),    
    INVALID_EXPIRE_DAY("Invalid expire days"),
    
    //------------
    //RED_WALLET
    //-----------
    STEP_ALREADY_DONE("Step already done"),
    STEP_DOES_NOT_BELONG_TO_PROMOTION("Step doesn't belog to the promotion selected"),
    INVALID_PROMOTION_OR_STEP("Invalid promotion or step value"),
    
    STEP_REAL_NAME_NOT_VALIDATED("User name has not been validated"),
    STEP_THERE_IS_NOT_NEWBIE_INVESTMENT(""),
    STEP_WECHAT_BINDING("Wechat account is not binded"),
    STEP_REGISTRATION_DONE("Registration is not finished"),
    STEP_NO_STEPS("There are not steps done for this promotions"),
    PROMOTION_INCOMPLETE("Missing promotion steps"),
    PROMOTION_NO_AMOUNT_TO_ASSIGN("There is not accumulated money to assign"),
    NOT_FUNNEL_USER("Error, not funnel user"),

    //-------------------------
    // ---- TRADER ACCOUNT ----
    //-------------------------
    TRADING_ACCOUNT_ERROR("There has been an error while loading your Trading Member Account information. Please try again"),
    ERROR_CANCELING_LOANS("There has been an error while canceling your Notes, please try again"),
    NOTES_CANNOT_BE_CANCELED("The Note(s) cannot be canceled."),
    NO_LOANS_TO_DELIST("You have not selected any Notes to cancel."),


    //-------------------------
    // ---- TRADER SELL -------
    //-------------------------
    LOAN_SELL_PROBLEM("There has been an error with your Note sell order, please try again"),
    ALREADY_SOLD("A Note fraction has already been sold"),

    DATE_TOO_EARLY("Please enter a date on or after today"),
    DATE_TOO_LATE("Please enter a date within the next 7 days."),
    INVALID_DATE_ENTERED("Please input valid date ranges"),
    INVALID_SINGLE_DATE_ENTERED("Please input a valid date"),
    
    INVALID_ORDER_ID("Invalid Order id"),
    INVALID_LOAN_ID("Invalid Loan id"),
    INVALID_PARAMETERS_SIZE_MISMATCH("Parameters size are inconsistent"),
    SELL_AMOUNT_LARGER_THAN_OUTSTANDING_PRNCP("Sell amount should be smaller than outstanding principal"),

    LOAN_PARTIAL_TRANSFER_NOT_SUPPORTED("Partial transfer is not supported."),
    GROUP_PARTIAL_TRANSFER_NOT_SUPPORTED("Partial transfer is not supported on the specified group-buy."),
    INVALID_PARTIAL_TRANSFER_AMOUNT("The specified sellAmount is invalid."),
    
    //-----------------------------
    // ---- TRADER DISCLOSURES ----
    //-----------------------------
    CERTIFY_TAXPAYER_ID("You must certify that your taxpayer identification number is correct"),
    CERTIFY_BACKUP_WITHHOLDING("You must certify that you are not subject to backup withholding"),
    ENTER_ELECTRONIC_SIGNATURE("You must enter your electronic signature"),
    CHECK_ELECTRONIC_SIGNATURE("You must accept the terms and conditions of the customer agreement"),
    ELECTRONIC_SIGNATURE_NOMATCH("The electronic signature you entered does not match your full name."),

    //-----------------------------
    // ---- REVIEW TRADER APPS ----
    //-----------------------------
    ERROR_OBTAINING_TRADER_APPS("There was an error obtaining the Trading Member Applications. Please try again."),
    COULD_NOT_OBTAIN_TRADER_APPS("We could not obtain the list of Trading Member Applications at this time. Please try again"),
    PLEASE_SELECT_REVIEW_TARGETS("No applicants were selected."),
    PLEASE_TRY_AGAIN_REVIEW_TRADER_APPS("We could not process your request at this time. Please try again."),
    MEMBER_INFO_UNAVAILABLE("This member's information is not available at this time. Please try again."),

    //---------------------------------
    // ----------TRADER INFO-----------
    //---------------------------------
    
    INVALID_CHARS_COMMENTS("The Comments may not have one or more of the following characters: <, >, =, --"),
    TOO_LONG_COMMENTS("Your loan description is too long. Please remove some characters.(Max 300)"),
    
    //---------------------------------
    // ---- COMPLETE LOAN PURCHASE ----
    //---------------------------------
    COMPLETE_LOAN_PURCHASE_ERROR("There has been an error while finalizing your buy order, please try again"),
    NOTES_CANNOT_BE_PROCESSED("One or more of your Notes cannot be processed"),
    NOT_ENOUGH_FUNDS_NOTES("You do not have sufficient funds to buy the selected Notes."),
    NOTE_PURCHASE_REQUEST_ERROR("Please try again, there has been an error with your buy order"),


    // ---- REPRICE NOTES ----

    NO_NOTES_TO_REPRICE("You have not selected any Note(s) to reprice"),


    //--------------------------------
    //----- Lending Match Verision 2 -- Invest UI
    //--------------------------------
    ENTER_AMOUNT_TO_INVEST_ABOVE_25("The minimum Amount to Invest is $25"),
    ENTER_MAX_PER_NOTE_ABOVE_25("You must enter at least $25 for the maximum amount per note"),
    ENTER_VALID_AMOUNT_TO_INVEST("Please enter a valid amount to invest"),
    ENTER_VALID_MAX_PER_NOTE("Please enter a maximum amount per note of at least $25"),
    UNABLE_TO_PROCESS_QUERY("We are unable to process your query at this time, please try again later."),
    UNABLE_TO_PROCESS_FILTER ("We are unable to process your query at this time, please try again later."),
    SESSION_EXPIRED("Your session has expired, please log back in and try again."),
    NO_INVENTORY("There are not enough Notes matching your criteria, please modify your settings."),
    UNABLE_TO_PROCESS_FILTER_BR ("Browse was unable to process your query. Please reset your filters and try again."),


    //--------------------------------
    //----- Lender account review
    //--------------------------------
    ACCOUNT_REVIEW_PRIVILEGE_MISSING("You are not allowed to access this page."),
    ACCOUNT_REVIEW_NOT_ALLOWED("You are not allowed to access this account."),
    ACCOUNT_DOES_NOT_EXIST("The user does not exist"),

    //--------------------------------
    //----- IRA Registation
    //--------------------------------
    SDIRA_FINANCIAL_INSTITUTION_MISSING("Please enter the name of your Financial Institution"),
    IRA_ACTION_MISSING("You must select an account type."),
    IRA_TYPE_MISSING("You must select an action."),
    IRA_PREFIX_MISSING("You must select a prefix."),
    IRA_INITIAL_MISSING("You must enter an initial."),
    IRA_MARITAL_MISSING("You must select a marital status."),
    MISSING_DDL ("The expiration date of your driver license is required"),
    INVALID_DDL ("A valid expiration date of your driver license is required"),
    INVALID_PRIMARY_PERCENTAGE ("The Primary beneficiaries percentages should add up to 100%"),
    INVALID_CONTIGENT_PERCENTAGE ("The Contingent beneficiaries percentages should add up to 100%"),
    MISSING_DL_STATE("The state in which your driver license was issued is required"),
    IRA_DLNUMBER_MISSING("You must enter your driver license number."),
    DL_RADIO_BUTTON_NOT_CHECKED("Please indicate your driver license status."),
    Q1_MISSING("You must answer the first question."),
    Q2_MISSING("You must answer the second question."),
    Q3_MISSING("You must answer the third question."),
    Q4_MISSING("You must answer the forth question."),
    Q5_MISSING("You must answer the fifth question."),
    
    ROLLOVER_Q1_MISSING("Please answer the first Yes/No question."),
    ROLLOVER_Q2_MISSING("Please answer the second Yes/No question."),
    ROLLOVER_Q3_MISSING("Please answer the third Yes/No question."),
    ROLLOVER_Q4_MISSING("Please answer the forth Yes/No question."),
    ROLLOVER_Q5_MISSING("Please answer the fifth Yes/No question."),
    ROLLOVER_Q6_MISSING("Please answer the sixth Yes/No question."),
    ROLLOVER_Q7_MISSING("Please answer the seventh Yes/No question."),
    ROLLOVER_Q8_MISSING("Please answer the eighth Yes/No question."),
    
    
    IRA_FUNDS_TRANSFER_AMOUNT("You must specify the intended transfer amount"),
    QUESTION_NOT_ANSWERED("Please answer all the questions listed below"),
    PRESENT_CUSTODIAN_INSTRUCTIONS("You must specify your Present Custodian Instructions"),
    LENDING_CLUB_TRANSFER_AMOUNT("Please enter a valid amount to transfer to your Lending Club Account"),

    LENDING_CLUB_TRANSFER_AMOUNT_EXCEEDS_IRA_TRANSFER_AMOUNT("Your LendingClub transfer amount exceeds your IRA transfer amount! Please ensure your IRA transfer amount is larger than your LendingClub transfer amount"),
   
    RMD_MISSING("Please choose how to process the Required Minimum Distribution or Life Expectancy Payment"),
    ENTER_TRANSFER_ACCOUNT_TYPE("Please enter a Transfer Account Type"),
    ENTER_TRANFER_PROCESS("Please choose how to process you transfer"),
    BENEFICIARY_NAME_MISSING("Please enter the beneficiary full name or the Trust name"),
    BENEFICIARY_SSN_EIN_MISSING("A beneficiary SSN or EIN is missing"),
    MISSING_PERCENTAGE("A beneficiary Percentage of Ownership is missing"),
    
    ENTER_TRANFER_PROCESS_AMOUNT("Please enter a valid partial amount to transfer"),
    
    INVALID_LICENSE_NUMBER("Please enter a valid driver license number"),
    INVALID_LICENSE_EXPIRATION_DATE("The driver license expiration date must be later than today"),

    
    LAST_YEAR_CONTRIBUTION_MISSING("Please enter a valid amount you have contributed for the relevant tax year(s)"),
    RELATIONS_MISSING("Please enter the beneficiary relationship information"),
    BENEFICIARIES_PERCENTAGE("If you have chosen any primary or contingent beneficiaries, please make sure their sums are 100%, separately."), 
    MUST_ACCEPT_COSIGNER_AGREEMENT("You must accept the co-signer agreement"), 
    MUST_ACCEPT_COSIGNER_MEMBERSHIP_AGREEMENTS("You must accept the co-signer membership agreement"), 
    MUST_ACCEPT_COSIGNER_NOTICE("You must accept the co-signer notice"),
    
    //
    // Contacts list validation errors
    //
    REQUIRES_TWO_FAMILY_MEMBERS("Please enter two family members"),
    REQUIRES_AT_LEAST_ONE_COWORKER("Please enter at least one coworker"),
    REQUIRES_AT_LEAST_ONE_FRIEND("Please enter at least one friend"),
    REQUIRES_ONE_EMERGENCY_CONTACT("Please enter an emergency contact"),
    DUPLICATE_CONTACT_NAME_NOT_ALLOWED("Please do not enter a user more than once"),
    REQUIRES_CONTACT_JOB_TITLE("A valid job title is required"),
    CONTACT_JOB_TITLE_TOO_LONG("Job title must not be longer than 40 characters"),
    //--------------------------------
    //----- Investor questions to borrowers
    //--------------------------------
    QUESTION_ALREADY_ASKED("You have already asked that question to this borrower!"),
    
    //LCA ITEMS
    ACCOUNT_NOT_FOUND("The account was not found"),
    NOT_AUTHORIZED_FOR_ACCOUNT("You are not authorized to view the account"),
    //LCA Daily Cash Transactions date filter
    EMPTY_FROM_DATE("Please select a \"From\" date. Only today's date and yesterday's date can be used"),
    EMPTY_TO_DATE("Please select a \"To\" date. Only today's date and yesterday's date can be used"),
    INVALID_DATE_RANGE("Only today's date and yesterday's date can be used"),
    NEGATIVE_DATE_RANGE("Please select a \"From\" date before the \"To\" date"),
    INVALID_FROM_DATE("Please enter a valid \"From\" date in the following format \"mm/dd/yyyy\""),
    INVALID_TO_DATE("Please enter a valid \"To\" date in the following format \"mm/dd/yyyy\""),
    DATE_LATER_THAN_TODAY("Please enter a valid date. Date can't be later than \"Today\""),
    DATE_LATER_THAN_YESTERDAY("Please enter a valid date. Date can't be later than \"Yesterday\""),
    
    INVALID_LOAN_SELECTED("Please select a valid loan offer"),
    GENDER_EMPTY("You must enter your gender"),
    INVALID_GENDER ("Invalid gender"),
    INVALID_BIRTHDAY("Please enter a valid birthdate in the following format \"mm/dd/yyyy\""),
    MISSING_MARITAL_STATUS ("A marital status is required"), INVALID_MARITAL_STATUS ("Invalid  marital status specified"),
    MISSING_HOUSING_STATUS ("A housing status is required"),
    INVALID_HOUSING_STATUS ("Invalid housing status"),
    MISSING_EDUCATION_LEVEL ("An education level is required"),
    INVALID_EDUCATION_LEVEL ("Invalid education level"),
    
    MISSING_ID_CARD ("ID card is required"),
    INVALID_ID_CARD ("ID card invalid"),
    INVALID_ID_CARD_LENGHT ("ID card is have 15 characters or 18 characters"),
    INVALID_ID_CARD_BIRTHDAY ("ID card invalid for the birthday invalid"),
    
    INVALID_BANK_CARD ("Bank card invalid"),
    
    // Loan application errors
    LOAN_APP_ALREADY_SUBMITTED ("Your loan application was already submitted"),
    MUST_SELECT_LOAN("You must select the loan type and amount"),
    MUST_PROVIDE_CONTACT_INFO ("You must provide your contact information before submitting your loan application"),
    MUST_PROVIDE_PERSONAL_INFO ("You must provide your personal information before submitting your loan application"),
    MUST_PROVIDE_EMPLOYMENT_INFO ("You must provide your employment information before submitting your loan application"),
    MUST_PROVIDE_BANK_ACCOUNT_INFO ("You must provide your bank account information before submitting your loan application"),
    MUST_UPLOAD_REQUIRED_DOCS("You must upload the required documents before submitting your loan application"),
    
    LOAN_AMOUNT_BELOW_MIN ("您输入的贷款金额低于最低贷款金额"),
    LOAN_AMOUNT_ABOVE_MAX ("您输入的贷款金额超过最高贷款金额"),
    MULTIPLE_LOAN_APPLICATIONS("You can not apply for more than 1 loan"),
    
    // Bank Account related errors
    MISSING_FINANCIAL_INSTITUTION("Bank name is required"),
    INVALID_FINANCIAL_INSTITUTION("Bank name is invalid"),
    MISSING_FINANCIAL_INSTITUTION_STATE("Bank account state is required"),
    INVALID_FINANCIAL_INSTITUTION_STATE("Bank account state is invalid"),
    MISSING_FINANCIAL_INSTITUTION_CITY("Bank account city is required"),
    INVALID_FINANCIAL_INSTITUTION_CITY("Bank account city is invalid"),
    INCONSISTENT_ACCOUNT("Your provide us inconsistent account number"),
    BRANCH_NAME_TOO_LONG ("Branch name is limited to 80 characters"),
    INVALID_CHARS_ERROR_BRANCH_NAME("Branch name may not have one or more of the following characters: <, >, =, --"),
    //Add funds error
    INVALID_FUNDING_AMT ("Please specify a valid funding amount"),
    INVALID_WITHDRAW_AMT ("Please specify a valid withdraw amount"),
    MISSING_WITHDRAW_AMOUNT("The withdraw amount is required!"),
    WITHDRAW_AMOUNT_GREATER_THAN_BALANCE("The withdraw amount is greater than your balance"),
    WITHDRAW_EXCEPTION("Exception happened, we already recorded it and will handle it later."),
    BANK_ACCOUNT_ALREADY_REGISTERED("The bank account is already registered by another investor."),
    WITHDRAW_DYNAMIC_ERROR("提现出错"),
    
    // partner invitation
    PARTNER_ALREADY_HAS_BEEN_INVITED ("This partner has already been invited"),
    
    //SMB error items
    MISSING_LOAN_APP("Please submit create and submit your loan application first!"),
    MISSING_AMOUNT("Amount is missing!"),
    MISSING_ITEMS("There are some missing items,please double check!"),
    HOUSE_OWNERS_TOO_LONG("House owners shall be less than 100 chars!"),
    HOUSE_ADDR_TOO_LONG("House address shall be less than 250 chars!"),
    HOUSE_ESTATE_NAME_TOO_LONG("House estate name shall be less than 250 chars!"),
    INVALID_BUY_HOUSE_DATE("Please specific the year you buy that house!"),
    INVALID_DATE_FORMAT("Date format is incorrect!"),
    INVALID_CHARS_ERROR_HOUSE_OWNERS("House owners may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_HOUSE_ADDR("House address may not have one or more of the following characters: <, >, =, --"),
    INVALID_CHARS_ERROR_HOUSE_ESTATE_NAME("House estate name may not have one or more of the following characters: <, >, =, --"),
    
    // MCA error items
    CA_INVALID_CHARS_ERROR_LINK_NAME("无效紧急联系人姓名"),
    MCA_MISSING_LINKMAN_CELL_PHONE("紧急联系人电话缺失"),
    MCA_INVALID_LINKMAN_CELL_PHONE("无效紧急联系人电话"),
    MCA_INVALID_LINKMAN_RELATION("无效紧急联系人关系"),
    MCA_INVALID_CHARS_ERROR_HOME_ADDRESS("无效紧急联系人地址"),
    MCA_INVALID_MID("merchant number should only contain letter,number or ','"),
    
    // Marketing channel
    MISSING_MARKETING_CHANNEL("Marketing channel is required"),
    INVALID_MARKETING_CHANNEL("Invalid marketing channel provided"),
    REQUIRED_OTHER_MARKETING_CHANNEL("Please enter the other channel description"),
	
    //Promotional codes
    MISSING_PROGRAM_ID("Program ID is empty"),
    INVALID_PROGRAM_ID("Invalid Program ID"),
    
    INVALID_CODE_COUNT("Invalid amount to generate"),
    INVALID_CODE_LENGTH("Invalid code length"),
    
    MISSING_PROMOTION_CODE("Promotion code is empty"),
    MISSING_THIRDPARTY_PAYMENT_COMPANY("Missing thirdparty payment company"),
    EXPIRED_PROMOTION_CODE("Promotion code has expired"),
    INVALID_PROMOTION_CODE("Invalid promotion code"),
    PROMOTION_CODE_ALREADY_USED("You can't use 2 promotion codes"),
    NOT_ENOUGH_FUNDS_FOR_PROMOTION("Added funds are not enough for the promotion bonus"),
    PROMOTION_FOR_LENDERS("This promotion is only for lenders"),
    
    INVALID_FROM_CODE_ID("From code ID is invalid"),
    INVALID_TO_CODE_ID("To code ID is invalid"),
    
	//Payment table
	EXCEPTION_IN_CAL("Exception happen when calculating the future payment."),
	
	// Transactions query
	INVALID_TRANSACTION_ID("Invalid transaction ID"),
	INVALID_TRANSACTION_TYPE("Invalid transaction type"),
	INVALID_TRANSACTION_STATUS("Invalid transaction status"),
	INVALID_TRANSACTION_RECON_STATUS("Invalid transaction reconciliation status"),
	
	INVALID_MIN_AMOUNT("Invalid minimum amount"),
	INVALID_MAX_AMOUNT("Invalid maximum amount"),
	DUPLICATE_ID_CARD ("Only one account per individual is allowed"),
	DUPLICATE_INSTITUTIONS_ID("Only one account per individual is allowed"),
	
	// Blacklist
	INVALID_BLACKLIST_ID("Invalid blacklist entry"),
	INVALID_BLACKLIST_TYPE("Invalid blacklist type"),
	INVALID_BLACKLIST_REASON_TYPE("Invalid blacklist reason type"),
	INVALID_BLACKLIST_STATUS("Invalid blacklist status"),
	EMPTY_BLACKLIST_VALUE("Blacklist value is empty"),
	EMPTY_BLACKLIST_REASON("Blacklist reason is empty"),
	EXISTENT_BLACKLIST_ENTRY("Blacklist entry already exists"),
	
	//VIP MODULE
	NOT_ENOUGT_VIP_LEVEL("Not enough VIP level to execute this operation"),
	VIP_FEATURE_INVALID_OPTION_TYPE("Please select a valid VIP feature type"),
	VIP_FEATURE_SUBMIT_FAILURE ("There was an error while submitting the VIP efature, pelase try again"),
	NO_SUCH_VIP_LEVEL_FOUND("No such VIP level found"),
	CANNOT_DELETE_VIP_LEVEL("Cannot delete VIP level"),
	FEATURE_DISABLED("The Feature has been temporarily disabled"),
	//SERVICES
	SERVICE_DISABLED_TEMPORARY("The service has been temporarily disabled"),
	ADD_MONEY_DISABLED("Add money service is not available"),
	WITHDRAW_MONEY_DISABLED("Withraw money service is not available"),
	//APP CONFIG
	CANNOT_SAVE_NEW_APP_CONFIG("Cannot save new app configuration"),
	//ERROR FOR AVAILABLE USERS
	APPLICATION_ONLY_FOR_LENDERS("The application is only for lender's use"),
	//PRIVILEGES
	NOT_ENOUGT_PRIVILEGE("Not enough privileges to execute this operation"),
	
	//Group/Plan etc.
	NOT_PERMITTED_TO_INVEST("Not permitted to invest on a loan"),

	//NEW BROWSE NOTES FOR SECONDARY MARKET
	TRADE_ALREADY_SOLD("The selected trade is already sold in secondary market"),
	
	INVALID_TRADE_ID("The trade is invalid"),
	
	INVALID_NUMBER_SPECIFIED("Invalid number!"),
	
	INTERNAL_SERVER_ERROR("Sorry, there is an internal error occured. Please contact our customer service."),
    
	// ================= Third Party Account Related Errors =====================
    NON_SUPPORTED_SOURCE_TYPE("Non-supported third party account"), 
    ALREADY_BIND_TO_OTHER_DR_ACCOUNT("This account has already been bound to other DR account"),
    USER_ALREADY_LOGGED_IN("User has already logged in"),
    MUST_INPUT_CAPTCHA("Must input captcha"),
    ACCOUNT_NOT_REGISTERED("Account not registered"),
    ACCOUNT_ALREADY_REGISTERED("Account already registered"),
    INVALID_ASSOCIATION("Invalid association"),
    BIND_TP_TO_ANOTHER_TP_PROHIBITED("Bind one third-party account to another third-party account prohibited"),
    
    EMAIL_CHANGE_NOT_CANCELED("Email change process could not be canceled, please contact Customer Support"),
    INVALID_THIRD_PARTY_VERIFY("Invalid third-party validation data"),
    MISSING_OPENID("Missing openid"),
    ALREADY_BIND_ANOTHER_SAME_TYPE_ACCOUNT("This accoutn has been bound to other third-party account"),
    INPUT_INFOR_NO_MATCH_LOGINED_ACTOR("You input information does not match the logined user"),
    
    //=================== Social profile messages================================
    ERROR_UPLOADING_PICTURE("Error Uploading Profile Picture"),
	ERROR_IN_PICTURE_REQUIREMENT("You can upload a JPG, GIF or PNG file (File size limit is 4 MB)."),
	
	//=================== roles and groups================================
    INVALID_ROLE_NAME("Empty role name"),
    USER_NOT_FOUND("user not found"),
    INVALID_ROLE_ID("invalid role id"),
    INVALID_GROUP_NAME("Invalid group name"),
    INVALID_GROUP_ID("invalid group id"),
    DUPLICATED_GROUP_NAME("Please check group name is not duplicated"),

    THIRD_PARTY_ACCOUNT_DISALLOWE_EXECUTE_UBIND("login by third-party account is not allow to execute ubind"), 
    UNABLE_TO_VOTE("Error when vote"),
    UNABLE_TO_VOTE_MORE_THAN_ONCE("A user is not allowed to vote more than once on a target"),
    UNABLE_TO_GET_VOTE_COUNT("Error when get vote count"), 
    INVALID_TIMES("Times must more than 0"), 
    INVALID_VOTE("invalid vote"), 
    FAIL_TO_CREATE_GROUP("Fail to create group"), 
    FAIL_GET_GROUP_ADMINS("Fail to admin group members"), 
    FAIL_GET_GROUP_MEMBERS("Fail to group members"), 
    FAIL_APPLY_FOR_MEMBER("Fail to apply for group member"),
    MAX_POST_REACHED("The max post number reached for today"),
    MISSING_ONE_KEY_PAY_ACCOUNT("Missing fast pay account!"),
    MISSING_ONE_KEY_PAY_ACCOUNT_RESERVED_PHONE("Missing  fast pay account reverved phone!"),
    MISSING_ONE_KEY_PAY_ORDER_ID("Missing fast pay order id!"),
    ACTOR_NOT_BIND_BAIDU_PUSH("The user is not bind baidu push service"),
	
	INVALID_TOKEN("The token is invalid"),
	TOKEN_IS_EXPIRED("The token is expired"),
	
	ONE_KEY_PAY_ACCOUNT_NOT_FOUND("The one key pay accounts was not found"),
	INVALID_ACCOUNT_ID("Account id is invalid"),
	ONE_KEY_PAY_ACCOUNT_NOT_SUPPORT("Your account is not support one key pay!"),
	ONE_KEY_PAY_ACCOUNT_IS_DISABLE("Your account is disable!"), 
	PASSPORT_LOCATION_EMPTY("Passport location couldn't be empty!"),
	DUPLICATE_PASSPORT("Passport has been verified"),
	PASSPORT_CAN_NOT_BE_UPDATED("Passport can not be updated"),
	UNSUPPORT_ACCOUNT_TYPE("unsupport this account type"),
	
	//Loan amount timer
	INVALID_TIMER_ID("The one key loan amount timer was not found"),
    INVALID_TIME_SPAN("You have specified an invalid time span"),
    INVALID_TIMER_NUMBER("You have specified an invalid timer number"),
    INVALID_TIMER_SCHEDULE_D("You have specified an invalid schedule day"),

	
	// Borrower API 
	BORROWER_STRING_TOO_LONG("字符串长度超过40字节"),
	BORROWER_LOAN_AMOUNT_ERROR("贷款金额错误"),
	BORROWER_LOAN_SUBTYPE_ERROR("贷款子类型错误"),
	BORROWER_LOAN_AMOUNT_TOO_LOW("贷款金额太低"),
	BORROWER_LOAN_AMOUNT_TOO_LARGE("贷款金额太高"),
	BORROWER_LOAN_AMOUNT_UNIT_ERROR("贷款金额必须为100倍数"),
	PLEASE_INPUT_LOAN_TYPE("Please select loan type"),
	ONLY_ALLOW_BORROWER_LOGIN("Only allow borrower login"),
	DATA_COUNT_ERROR("data count error"),
	FIELD_ERROR("field error"),
	NOT_PUBLIC_DR_USER("Don't public to dianrong user"),
	MORE_THAN_MAX_REGISTER("more than max registrations"),
	INVITE_CODE_NOT_EXSIT("Invite code is wrong"),
	APPLICATION_NOT_FOR_XIAOMING("The application is not for xiaomingactor's use"),
	INTEREST_INVALID("The interest is invalid"),
	NOT_FIND_GENERATOR("Not find generator"),
    INVALID_GENERATOR_CLASS("Invalid generator class"),
    INVALID_COMPANY_GUARANTEE_TYPE("Invalid type of company guarantee"),
    INVALID_MORTGAGE_PLEDGE_TYPE("Invalid type of mortgage-pledge"),
    INVALID_EVALUATE_TYPE("Invalid type of evaluate"),
    INVALID_PARAMETERS("The interest or principal is invalid"),
    INVALID_FAMILY_ANNUAL_INCOME("Invalid family annual income"),
    BORROWER_REMARK_TOO_LONG("备注信息过长,超过500字"),
    PLEASE_UPGRADE_APP("版本过低，请升级至最新版。"),
    ;
    // ======================================
	
	
    
	private String _label;
    
    private RequestErrorKey(String label) {
    	this._label = label;
    }
    
    public String getLabel() {
    	return this._label;
    }
    
    public void setLabel(String label) {
    	this._label = label;
    }
    
    public String toString() {
    	return this._label;
    }
    
}
