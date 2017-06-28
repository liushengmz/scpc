
function Provinces() { }
Provinces.prototype._data = ['北京', '江苏', '安徽', '湖北', '天津', '山东', '上海', '广东',
    '浙江', '广西', '甘肃', '吉林', '辽宁', '内蒙古', '新疆', '黑龙江', '河北', '重庆', '四川',
    '陕西', '福建', '海南', '江西', '山西', '湖南', '河南', '青海', '贵州', '宁夏', '云南', '西藏', '未知'];

Provinces.prototype.getNameById = function (provinceId) {
    return this._data[provinceId - 1];
}

Provinces.prototype.getNamesByIds = function (provinceIds) {
    var names = [];
    for (var i = 0; i < provinceIds.length; i++)
        names.push(this.getNameById(provinceIds[i]));
    return names.sort();
}
