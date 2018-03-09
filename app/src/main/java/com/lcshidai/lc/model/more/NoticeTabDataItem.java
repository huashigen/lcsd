package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.account.AccountSettingImg;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeTabDataItem {
	public String id;
	public String title;
	public String type_display;
	public String time_display;
	public String content;
	public AccountSettingImg thumb;
	public String is_read;
	public String img_path;
	public String source;

	public NoticeTabDataItem() {

	}

	public NoticeTabDataItem(String me) {

	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getIs_read() {
		return is_read;
	}

	@JsonProperty("is_read")
	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	@JsonProperty("thumb")
	public void setThumb(AccountSettingImg thumb) {
		this.thumb = thumb;
	}

	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType_display() {
		return type_display;
	}

	@JsonProperty("type_display")
	public void setType_display(String type_display) {
		this.type_display = type_display;
	}

	public String getTime_display() {
		return time_display;
	}

	@JsonProperty("time_display")
	public void setTime_display(String time_display) {
		this.time_display = time_display;
	}

	public String getContent() {
		return content;
	}

	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	public AccountSettingImg getThumb() {
		return thumb;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}

//	public class Thumb {
//		String url;
//		String url_s700;
//		String url_s300;
//		String url_s100;
//
//		public Thumb() {
//
//		}
//
//		public Thumb(String me) {
//
//		}
//
//		public String getUrl() {
//			return url;
//		}
//
//		@JsonProperty("url")
//		public void setUrl(String url) {
//			this.url = url;
//		}
//
//		public String getUrl_s700() {
//			return url_s700;
//		}
//
//		@JsonProperty("url_s700")
//		public void setUrl_s700(String url_s700) {
//			this.url_s700 = url_s700;
//		}
//
//		public String getUrl_s300() {
//			return url_s300;
//		}
//
//		@JsonProperty("url_s300")
//		public void setUrl_s300(String url_s300) {
//			this.url_s300 = url_s300;
//		}
//
//		public String getUrl_s100() {
//			return url_s100;
//		}
//
//		@JsonProperty("url_s100")
//		public void setUrl_s100(String url_s100) {
//			this.url_s100 = url_s100;
//		}
//
//	}
}
