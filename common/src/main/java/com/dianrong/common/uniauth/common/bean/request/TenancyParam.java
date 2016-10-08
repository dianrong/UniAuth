package com.dianrong.common.uniauth.common.bean.request;

public class TenancyParam extends PageParam {
	private static final long serialVersionUID = -1587818665974806460L;

	private Long id;

    private String code;

    private String name;

    private String contactName;

    private String phone;

    private String description;

    private Byte status;

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getContactName() {
		return contactName;
	}

	public String getPhone() {
		return phone;
	}

	public String getDescription() {
		return description;
	}

	public Byte getStatus() {
		return status;
	}

	public TenancyParam setId(Long id) {
		this.id = id;
		return this;
	}

	public TenancyParam setCode(String code) {
		this.code = code;
		return this;
	}

	public TenancyParam setName(String name) {
		this.name = name;
		return this;
	}

	public TenancyParam setContactName(String contactName) {
		this.contactName = contactName;
		return this;
	}

	public TenancyParam setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public TenancyParam setDescription(String description) {
		this.description = description;
		return this;
	}

	public TenancyParam setStatus(Byte status) {
		this.status = status;
		return this;
	}
}
