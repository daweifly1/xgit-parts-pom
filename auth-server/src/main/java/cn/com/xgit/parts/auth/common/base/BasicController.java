package cn.com.xgit.parts.auth.common.base;

import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class BasicController {
    public <T> ResultMessage<T> ResultMessage(ErrorCode code, T value) {
        return ResultMessage.success(code.getCode(), code.getDesc(), value);
    }

    public <T> ResultMessage<T> ResultMessage(T value) {
        ErrorCode code = ErrorCode.Success;
        return ResultMessage(code, value);
    }

    public <T> ResultMessage<T> actionErrorResult(String errorMsg) {
        ErrorCode code = ErrorCode.Failure;
        return ResultMessage.error(code.getCode(), errorMsg, null);
    }

    public <T> ResultMessage<T> actionErrorResult(int code, String errorMsg) {
        return ResultMessage.error(code, errorMsg, null);
    }

    public ResultMessage ResultMessage(ErrorCode code) {
        return ResultMessage(code, null);
    }

    public Long getUserId(HttpServletRequest request) {
        String userId = request.getHeader("x-user-id");
        if (StringUtils.isNoneBlank(userId)) {
            return Long.parseLong(userId);
        }
        return null;
    }

    public SysAccountVO getSysAccountVO(HttpServletRequest request) {
        Long uid = getUserId(request);
        if (null != uid) {
            SysAccountVO sysAccountVO = new SysAccountVO();
            sysAccountVO.setId(uid);
            return sysAccountVO;
        }
        return null;
    }

    public String getRemoteIp(HttpServletRequest request) {
        String userIp = request.getHeader("x-remote-ip");
        return userIp;
    }

    /**
     * 获取分页参数
     *
     * @param <T>
     * @return
     */
    protected <T> Page<T> getPagination() {
        HttpServletRequest request = getHttpRequest();
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "20";
        }
        String currentPage = request.getParameter("current");
        if (StringUtils.isEmpty(currentPage)) {
            currentPage = "1";
        }
        Assert.isTrue(StringUtils.isNumeric(pageSize), "分页参数：pageSize参数不是数字");
        Assert.isTrue(StringUtils.isNumeric(currentPage), "分页参数：current参数不算数字");
        int size = Integer.valueOf(pageSize);
        int current = Integer.valueOf(currentPage);
        String ascs = request.getParameter("ascs");//排序字段
        String descs = request.getParameter("descs");//升序降序
        Page<T> page = new Page<>(current, size);
        if (!StringUtils.isEmpty(ascs)) {
            page.setAsc(ascs.split(","));
        }
        if (!StringUtils.isEmpty(descs)) {
            page.setDesc(descs.split(","));
        }
        return page;
    }

    HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public ResultMessage<Object> deleteBatchIds(String ids, SuperService superService) {
        if (StringUtils.isNotBlank(ids)) {
            List idList = Arrays.asList(ids.split(","));
            if (superService.removeByIds(idList))
                return ResultMessage.success(ResultMessage.OK, "删除成功");
            return ResultMessage.error(ResultMessage.FAIL, "删除失败");
        }
        return ResultMessage.error(ResultMessage.FAIL, "删除失败:传入参数为空");
    }
}
