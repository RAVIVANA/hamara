package com.nkxgen.spring.jdbc.DaoInterfaces;

import com.nkxgen.spring.jdbc.model.BankUser;
import com.nkxgen.spring.jdbc.model.Permission;

public interface PermissionsDAOInterface {

    void updatePermissions(Permission permissions);

    void allUpdatePermissions(Permission permissions);

    Permission getPermissions(Long id);

    BankUser getUserById(int id);
}
