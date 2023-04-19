package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.FriendChain;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.FriendChainMapper;
import top.kuoer.service.FriendChainService;

import java.util.List;

@Service
public class FriendChainServiceImpl implements FriendChainService {

    private final FriendChainMapper friendChainMapper;

    @Autowired
    public FriendChainServiceImpl(FriendChainMapper friendChainMapper) {
        this.friendChainMapper = friendChainMapper;
    }

    @Override
    public Result addFriendChain(FriendChain friendChain) {
        if("".equals(friendChain.getTitle()) || "".equals(friendChain.getSubtitle()) || "".equals(friendChain.getUrl()) || "".equals(friendChain.getIcon())) {
            return new Result(ResultCode.OPERATIONFAIL, "提交的必要参数不能为空");
        }

        if(this.friendChainMapper.addFriendChain(friendChain)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }


    @Override
    public Result findFriendChainAll() {
        return new Result(ResultCode.SUCCESS, friendChainMapper.findFriendChainAll());
    }

    @Override
    public Result modifyFriendChain(int id, String title, String subTitle, String url, String icon) {
        if(this.friendChainMapper.modifyFriendChain(id, title, subTitle, url, icon) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result removeFriendChain(int id) {
        if(this.friendChainMapper.removeFriendChain(id) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

}
