package com.xingyi.logistic.controller;

import com.xingyi.logistic.business.bean.BaseQueryPage;
import com.xingyi.logistic.business.service.BaseService;
import com.xingyi.logistic.business.util.PrimitiveUtil;
import com.xingyi.logistic.common.bean.ErrCode;
import com.xingyi.logistic.common.bean.JsonRet;
import com.xingyi.logistic.config.JsonParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jadic on 2018/1/1.
 */
public abstract class BaseCRUDController<Model, Condition extends BaseQueryPage> extends BaseController {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonRet<Long> add(@JsonParam Model model) {
        return getBaseService().add(model);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public JsonRet<Boolean> modify(@JsonParam Model model) {
        return getBaseService().modify(model);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public JsonRet<Boolean> del(@RequestParam Long id) {
        return getBaseService().del(id);
    }

    @RequestMapping("/getById")
    public JsonRet<Model> getById(@RequestParam Long id) {
        return getBaseService().getById(id);
    }

    @RequestMapping("/getList")
    public JsonRet<Object> getList(@JsonParam Condition condition) {
        BaseService<Model, Condition> service = getBaseService();
        JsonRet<Integer> totalRet = service.getTotal(condition);
        if (totalRet.isSuccess()) {
            Map<String, Object> params = new HashMap<>();
            if (PrimitiveUtil.getPrimitive(totalRet.getData(), 0) > 0) {
                JsonRet<List<Model>> listRet = service.getList(condition);
                if (listRet.isSuccess()) {
                    params.put("total", totalRet.getData());
                    params.put("list", listRet.getData());
                    return JsonRet.getSuccessRet(params);
                } else {
                    return JsonRet.getErrRet(ErrCode.GET_ERR.getCode(), listRet.getMsg());
                }
            } else {
                params.put("total", 0);
                return JsonRet.getSuccessRet(params);
            }
        }
        return JsonRet.getErrRet(ErrCode.GET_ERR.getCode(), totalRet.getMsg());
    }

    protected abstract BaseService<Model, Condition> getBaseService();
}
