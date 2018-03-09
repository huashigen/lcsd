package com.lcshidai.lc.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class BankCardInfo implements Parcelable {
	String id;
	String bank_name;
	String bank;
	String acount_name;
	String sub_bank;
	String code;
	String channel;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getAcount_name() {
		return acount_name;
	}
	public void setAcount_name(String acount_name) {
		this.acount_name = acount_name;
	}
	public String getSub_bank() {
		return sub_bank;
	}
	public void setSub_bank(String sub_bank) {
		this.sub_bank = sub_bank;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(bank_name);
		dest.writeString(bank);
		dest.writeString(acount_name);
	}
	
	public static final Parcelable.Creator<BankCardInfo> CREATOR = new Creator<BankCardInfo>() {

		@Override
		public BankCardInfo createFromParcel(Parcel source) {
			BankCardInfo info = new BankCardInfo();
			info.id = source.readString();
			info.bank_name = source.readString();
			info.bank = source.readString();
			info.acount_name = source.readString();
			return info;
		}

		@Override
		public BankCardInfo[] newArray(int size) {
			return new BankCardInfo[size];
		}
		
	};
	
}
