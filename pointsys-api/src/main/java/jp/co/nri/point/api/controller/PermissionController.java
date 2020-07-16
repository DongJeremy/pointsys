package jp.co.nri.point.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.domain.Permission;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.util.UserUtil;

@Tag(name = "权限")
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    @Operation(summary = "当前登录用户拥有的权限")
    @GetMapping("/current")
    public List<Permission> permissionsCurrent() {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Permission> list = loginUser.getPermissions();
        final List<Permission> permissions = list.stream().filter(li -> li.getType().equals(1))
                .collect(Collectors.toList());
        List<Permission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L))
                .collect(Collectors.toList());
        firstLevel.parallelStream().forEach(p -> {
            setChild(p, permissions);
        });

        return firstLevel;
    }

    private void setChild(Permission p, List<Permission> permissions) {
        List<Permission> child = permissions.parallelStream().filter(a -> a.getParentId().equals(p.getId()))
                .collect(Collectors.toList());
        p.setChild(child);
        if (!CollectionUtils.isEmpty(child)) {
            child.parallelStream().forEach(c -> {
                setChild(c, permissions);
            });
        }
    }

}
