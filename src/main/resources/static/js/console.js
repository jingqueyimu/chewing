/**
 * 分页搜索
 * 
 * @param url
 * @param pageInfo
 * @param formSelector Form选择器
 * @param numKeys value为数值类型的key
 * @param boolKeys value为布尔类型的key
 */
function pageSearch(url, pageInfo, formSelector, numKeys, boolKeys) {
    if (pageInfo == undefined) {
        return;
    }
    var pageNum = pageInfo.pageNum;
    var pageSize = pageInfo.pageSize;
    var pages = pageInfo.pages;
    pageNum = pageNum < 1 ? 1 : pageNum;
    pageNum = pageNum > pages ? pages : pageNum;
    var params = formToJsonString($(formSelector), numKeys, boolKeys);
    location.href = url + "?pageNum=" + pageNum + "&pageSize=" + pageSize + "&params=" + encodeURI(params);
}
