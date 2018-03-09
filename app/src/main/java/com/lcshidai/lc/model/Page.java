package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page extends BaseData {
    private int page;
    private String pageSize;// :每页的条数,
    private String currentPage;// :当前的页数,
    private int totalPages;// :总共的页数,
    private String totalRows;// :总共的条数
    private String total;// :总共的条数

    public String getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(String total) {
        this.total = total;
    }

    public Page() {

    }

    public Page(String me) {

    }

    public int getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(int page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    @JsonProperty("pageSize")
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    @JsonProperty("current_page")
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @JsonProperty("total_page")
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalRows() {
        return totalRows;
    }

    @JsonProperty("totalRows")
    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

}
