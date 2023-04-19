package top.kuoer.controller.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.entity.FriendChain;
import top.kuoer.service.FriendChainService;

import java.util.List;

@RestController
@RequestMapping(path="/friendchain")
public class FriendChainController {

    private final FriendChainService friendChainService;

    @Autowired
    public FriendChainController(FriendChainService friendChainService) {
        this.friendChainService = friendChainService;
    }

    @RequestMapping(path="/findFriendChainAll", method = RequestMethod.GET)
    public Result findFriendChainAll() {
        return friendChainService.findFriendChainAll();
    }

    @RequestMapping(path="/modifyFriendChain", method = RequestMethod.POST)
    public Result modifyFriendChain(int id, String title, String subTitle, String url, String icon) {
        return friendChainService.modifyFriendChain(id, title, subTitle, url, icon);
    }

    @RequestMapping(path="/addFriendChain", method = RequestMethod.POST)
    public Result addFriendChain(FriendChain friendChain) {
        return friendChainService.addFriendChain(friendChain);
    }

    @RequestMapping(path="/removeFriendChain", method = RequestMethod.GET)
    public Result removeFriendChain(int id) {
        return friendChainService.removeFriendChain(id);
    }

}
