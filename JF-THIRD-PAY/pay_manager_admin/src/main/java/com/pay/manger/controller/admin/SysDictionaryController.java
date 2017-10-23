package com.pay.manger.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.business.util.ParameterEunm;

/**
 * @ClassName: SysDictionaryController
 * @Description: 字典控制器
 * @author buyuer
 * @date 2015年11月25日 上午10:46:10
 * 
 */
@RequestMapping("/sysDic/*")
@Controller
public class SysDictionaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysDictionaryController.class);
    @Autowired
    private SysConfigDictionaryService sysConfigDictionaryService;

    /**
     * 
     * @author buyuer
     * @Title: dicList
     * @Description: 获取字典目录
     */
    @RequestMapping("/dicList")
    public ModelAndView dicTopList(@RequestParam Map<String, Object> map) {
        ModelAndView andView = new ModelAndView("system/dic-list");
        if (map == null || map.isEmpty()) {
            map.put("dictPvalue", -1);
        } else {
            if (map.get("dictPvalue") == null) {
                map.put("dictPvalue", -1);
            } else {
                try {
                    Integer dictPvalue = Integer.parseInt(map.get("dictPvalue").toString());
                    SysConfigDictionary dictionary = sysConfigDictionaryService.findById(dictPvalue);
                    andView.addObject("dic", dictionary);
                } catch (NumberFormatException e) {
                    LOGGER.error("查询字典列表异常", e);
                }
                
            }
        }
        PageObject<SysConfigDictionary> dicList = sysConfigDictionaryService.Pagequery(map);
        andView.addObject("dicList", dicList);
        return andView;
    }
    
    @RequestMapping("/goAddOrUpdDic")
    public ModelAndView goAddOrUpdDic(@RequestParam(value = "dictPvalue", required = false) Integer dictPvalue, @RequestParam(value = "id", required = false) Integer id){
        ModelAndView andView = null;
        if (id != null) {
            SysConfigDictionary dictionary = sysConfigDictionaryService.findById(id);
            if (dictionary != null) {
                andView = new ModelAndView("system/dic-upd");
                andView.addObject("dic", dictionary);
            } else {
                andView = new ModelAndView("404");
            }
        } else {
            andView = new ModelAndView("system/dic-add");
        }
        andView.addObject("dictPvalue", dictPvalue);
        return andView;
    }
    
    @ResponseBody
    @RequestMapping("addDic")
    public Map<String, Object> addDic(@RequestParam(value = "dictName", required = true) String dictName, @RequestParam(value = "dictValue", required = true) String dictValue, @RequestParam(value = "dictPvalue", required = true) Integer dictPvalue) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            boolean isExits = sysConfigDictionaryService.findIsExits(dictName, dictPvalue);
            if (!isExits) {
                SysConfigDictionary dictionary = new SysConfigDictionary();
                dictionary.setDictName(dictName);
                dictionary.setDictValue(dictValue);
                dictionary.setDictPvalue(dictPvalue);
                dictionary.setCurrType(0);
                sysConfigDictionaryService.add(dictionary);
                resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
            } else {
                resultMap = ReMessage.resultBack(ParameterEunm.ERROR_403_CODE, null);
            }
        } catch (Exception e) {
            LOGGER.error("添加字典数据失败");
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
        }
        return resultMap;
    }
    
    
    @ResponseBody
    @RequestMapping("updDic")
    public Map<String, Object> updDic(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            sysConfigDictionaryService.update(map);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            LOGGER.error("添加字典数据失败");
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
        }
        return resultMap;
    }
    
    
    @ResponseBody
    @RequestMapping("/isExits")
    public Map<String, Object> isExits(@RequestParam(value = "dictName", required = true) String dictName, String dicValue, @RequestParam(value = "dictPvalue", required = true) Integer dictPvalue){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean isExits = sysConfigDictionaryService.findIsExits(dictName, dictPvalue);
        if (isExits) {
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } else {
            resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
        }
        return resultMap;
    }
    
    @ResponseBody
    @RequestMapping("/lockDic")
    public Map<String, Object> lockDic(@RequestParam(value = "id") Integer id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            SysConfigDictionary dictionary = new SysConfigDictionary();
            dictionary.setId(id);
            dictionary.setCurrType(1);
            sysConfigDictionaryService.update(dictionary);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
            LOGGER.error("锁定失败." , e);
        }
        return resultMap;
    }
    
    @ResponseBody
    @RequestMapping("openDic")
    public Map<String, Object> openDic(@RequestParam(value = "id") Integer id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            SysConfigDictionary dictionary = new SysConfigDictionary();
            dictionary.setId(id);
            dictionary.setCurrType(0);
            sysConfigDictionaryService.update(dictionary);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
            LOGGER.error("锁定失败." , e);
        }
        return resultMap;
    }
    
    @ResponseBody
    @RequestMapping("delDic")
    public Map<String, Object> delDic(@RequestParam(value = "id") Integer id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            SysConfigDictionary dictionary = new SysConfigDictionary();
            dictionary.setId(id);
            sysConfigDictionaryService.delete(dictionary);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
            LOGGER.error("锁定失败." , e);
        } catch (Throwable e) {
            resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
            LOGGER.error("锁定失败." , e);
        }
        return resultMap;
    }
}
