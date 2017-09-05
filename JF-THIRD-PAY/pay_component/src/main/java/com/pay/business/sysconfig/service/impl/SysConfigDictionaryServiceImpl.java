package com.pay.business.sysconfig.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.cache.annotations.Cacheable;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.mapper.SysConfigDictionaryMapper;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;

/**
 * @author buyuer
 * @version
 */
@Service("sysConfigDictionaryService")
public class SysConfigDictionaryServiceImpl extends BaseServiceImpl<SysConfigDictionary, SysConfigDictionaryMapper> implements SysConfigDictionaryService {
    // 注入当前dao对象
    @Autowired
    private SysConfigDictionaryMapper sysConfigDictionaryMapper;

    public SysConfigDictionaryServiceImpl() {
        setMapperClass(SysConfigDictionaryMapper.class, SysConfigDictionary.class);
    }

    /*
     * (非 Javadoc) <p>Title: getDic</p> <p>Description: </p>
     * 
     * @param type
     * 
     * @param value
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigDictionaryService#getDic(int, java.lang.String)
     */
    public SysConfigDictionary getDic(int type, String value) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        dictionary.setDictPvalue(type);
        dictionary.setDictValue(value);
        dictionary.setCurrType(0);
        return sysConfigDictionaryMapper.selectSingle(dictionary);
    }

    
    public SysConfigDictionary getDic(String typeCode, String value) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        dictionary.setDictValue(typeCode);
        dictionary.setCurrType(0);
        dictionary = sysConfigDictionaryMapper.selectSingle(dictionary);
        if (dictionary != null) {
            return getDic(dictionary.getId(), value);
        } else {
            return null;
        }
    }
    /*
     * (非 Javadoc) <p>Title: getDicList</p> <p>Description: </p>
     * 
     * @param type
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigDictionaryService#getDicList(int)
     */
    public List<SysConfigDictionary> getDicList(int type) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        if (type != -1) {
            dictionary.setDictPvalue(type);
        }
        dictionary.setCurrType(0);
        return sysConfigDictionaryMapper.selectByObject(dictionary);
    }

    /*
     * (非 Javadoc) <p>Title: getDicList</p> <p>Description: </p>
     * 
     * @param typeCode
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigDictionaryService#getDicList(java.lang.String)
     */
    public List<SysConfigDictionary> getDicList(String typeCode) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        dictionary.setDictValue(typeCode);
        dictionary.setCurrType(0);
        dictionary = sysConfigDictionaryMapper.selectSingle(dictionary);
        SysConfigDictionary configDictionary = new SysConfigDictionary();
        configDictionary.setCurrType(0);
        configDictionary.setDictPvalue(dictionary.getId());
        return sysConfigDictionaryMapper.selectByObject(configDictionary);
    }

    /*
     * (非 Javadoc) <p>Title: findIsExits</p> <p>Description: </p>
     * 
     * @param dicName
     * 
     * @param dicPvalue
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigDictionaryService#findIsExits(java.lang.String, java.lang.Integer)
     */
    public boolean findIsExits(String dicName, Integer dicPvalue) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        dictionary.setDictPvalue(dicPvalue);
        dictionary.setDictName(dicName);
        dictionary = sysConfigDictionaryMapper.selectSingle(dictionary);
        return dictionary != null ? true : false;
    }

    /*
     * (非 Javadoc) <p>Title: findById</p> <p>Description: </p>
     * 
     * @param id
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigDictionaryService#findById(java.lang.Integer)
     */
    @Cacheable(
			region = "sysConfigDictionaryService",
			namespace = "findById",
			fieldsKey = {"#id"},
			expire = 300
			)
    public SysConfigDictionary findById(Integer id) {
        SysConfigDictionary dictionary = new SysConfigDictionary();
        dictionary.setId(id);
        dictionary.setCurrType(0);
        return sysConfigDictionaryMapper.selectSingle(dictionary);
    }

}
