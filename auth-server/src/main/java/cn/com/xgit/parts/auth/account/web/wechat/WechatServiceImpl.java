package cn.com.xgit.parts.auth.account.web.wechat;

import cn.com.xgit.parts.auth.VO.ProfileVO;
import cn.com.xgit.parts.auth.account.dao.entity.ThirdpartyOauthDO;
import cn.com.xgit.parts.auth.account.dao.mapper.ThirdpartyOauthMapper;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WechatServiceImpl
        implements WechatService {
    @Autowired
    private ThirdpartyOauthMapper thirdpartyOauthMapper;

    public String queryUserIdByWechat(WxMpUser wxMpUser) {
        String openId = wxMpUser.getOpenId();

        ThirdpartyOauthDO thirdpartyOauthDO = this.thirdpartyOauthMapper.itemByThirdpartyId(openId);
        if ((thirdpartyOauthDO != null) && (thirdpartyOauthDO.getBindStatus() != null) && (thirdpartyOauthDO.getBindStatus().equals(Integer.valueOf(1)))) {
            return thirdpartyOauthDO.getUserId();
        }
        return null;
    }

    public ProfileVO generateProfile(String roleId, WxMpUser wxMpUser) {
        ProfileVO profileVO = new ProfileVO();
        profileVO.setNickname(wxMpUser.getNickname());
        profileVO.setName(wxMpUser.getNickname());
        profileVO.setLoginName(wxMpUser.getNickname());
        profileVO.setSex(wxMpUser.getSex());
        profileVO.setLocked(Integer.valueOf(0));
        List<String> roleList = new ArrayList();
        roleList.add(roleId);
        profileVO.setRoleIds(roleList);
        return profileVO;
    }
}
