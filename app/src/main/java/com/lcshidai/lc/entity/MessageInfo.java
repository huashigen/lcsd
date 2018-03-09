package com.lcshidai.lc.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class MessageInfo implements Parcelable {
	public String id;
	public String remind_title;
	public String remind_message;
	public String show_time;
	public String remind_type;
	public String prj_type;	//速转让-H
	public String prj_id;
	
	public static final Parcelable.Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {

		@Override
		public MessageInfo createFromParcel(Parcel source) {
			MessageInfo info = new MessageInfo();
			info.id = source.readString();
			info.remind_title = source.readString();
			info.remind_message = source.readString();
			info.show_time = source.readString();
			info.remind_type = source.readString();
			info.prj_type = source.readString();
			info.prj_id = source.readString();
			return info;
		}

		@Override
		public MessageInfo[] newArray(int size) {
			return new MessageInfo[size];
		}
		
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(remind_title);
		dest.writeString(remind_message);
		dest.writeString(show_time);
		dest.writeString(remind_type);
		dest.writeString(prj_type);
		dest.writeString(prj_id);
	}
}