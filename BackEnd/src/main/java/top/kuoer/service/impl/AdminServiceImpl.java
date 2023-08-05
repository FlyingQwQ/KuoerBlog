package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.Admin;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.AdminMapper;
import top.kuoer.service.AdminService;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin login(String userName, String password) {
        Admin admin = this.adminMapper.findIdByNameAndPassword(userName, password);
        if(null != admin) {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            admin.setToken(uuid);
            admin.setName(userName);
            this.adminMapper.setUserToken(admin);
            return admin;
        }
        return new Admin(-1, null, null, null);
    }

    @Override
    public Result verification(String token) {
        Admin admin = this.adminMapper.findIdByToken(token);
        if(null != admin) {
            return new Result(ResultCode.SUCCESS, admin);
        }
        return new Result(ResultCode.NOAUTH, null);
    }


    @Override
    public Result addAdmin(String userName, String password) {
        if(null == this.adminMapper.checkRepeat(userName)) {
            if(this.adminMapper.addAdmin(userName, password) > 0) {
                return new Result(ResultCode.SUCCESS, null);
            }
            return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
        }
        return new Result(ResultCode.OPERATIONFAIL, "该账号已经存在了");
    }

    @Override
    public List<Admin> adminList() {
        return this.adminMapper.adminList();
    }

    @Override
    public Result removeAdmin(int id) {
        if(this.adminMapper.removeAdmin(id) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result modifyAdmin(String name, String password) {
        if(this.adminMapper.modifyAdmin(name, password) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }



    @Override
    public Admin _verification(String token) {
        Admin admin = this.adminMapper.findIdByToken(token);
        if(null != admin) {
            return admin;
        }
        return new Admin(-1, null, null, null);
    }

}
