package com.nkxgen.spring.jdbc.Test;
import com.nkxgen.spring.jdbc.Dao.BankUserService;
import org.testng.annotations.*;

import com.nkxgen.spring.jdbc.DaoInterfaces.BankUserInterface;
import com.nkxgen.spring.jdbc.model.BankUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


public class BankUserServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BankUserService bankUserService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveBankUser() {
        BankUser bankUser = new BankUser();

        BankUser result = bankUserService.saveBankUser(bankUser);

        verify(entityManager).persist(bankUser);
        assert result==bankUser;
    }

    @Test
    public void testGetBankUserById() {
        long busrId = 1L;
        BankUser expectedBankUser = new BankUser();
        when(entityManager.find(BankUser.class, busrId)).thenReturn(expectedBankUser);

        BankUser bankUser = bankUserService.getBankUserById(busrId);

        verify(entityManager).find(BankUser.class, busrId);
        assert bankUser == expectedBankUser;
    }

    @Test
    public void testGetAllBankUsers() {
        List<BankUser> expectedBankUsers = new ArrayList<>();
        Query query = Mockito.mock(Query.class);
        when(entityManager.createQuery("SELECT u FROM BankUser u ORDER BY u.busr_id ASC")).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedBankUsers);

        List<BankUser> bankUsers = bankUserService.getAllBankUsers();

        verify(entityManager).createQuery("SELECT u FROM BankUser u ORDER BY u.busr_id ASC");
        verify(query).getResultList();
        assert bankUsers == expectedBankUsers;
    }


    @Test
    public void testSaveUser() {
        BankUser bankUser = new BankUser();
        // Set the properties of the bankUser object

        bankUserService.saveUser(bankUser);

        verify(entityManager).merge(bankUser);
    }


    @Test
    public void testGetBankUsersByDesignation() {
        String designation = "Manager";
        List<BankUser> expectedBankUsers = new ArrayList<>();
        BankUser b=new BankUser();
        expectedBankUsers.add(b);
        TypedQuery<BankUser> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT bu FROM BankUser bu WHERE bu.busr_desg = :designation", BankUser.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("designation", designation)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedBankUsers);

        List<BankUser> bankUsers = bankUserService.getBankUsersByDesignation(designation);

        verify(entityManager).createQuery("SELECT bu FROM BankUser bu WHERE bu.busr_desg = :designation", BankUser.class);
        verify(typedQuery).setParameter("designation", designation);
        verify(typedQuery).getResultList();
        assert bankUsers == expectedBankUsers;
    }

    @Test
    public void testGetBankUsersByDesignation_BankUser() {
        BankUser bankUser = new BankUser();
        bankUser.setBusr_desg("Manager");
        List<BankUser> expectedBankUsers = new ArrayList<>();
        BankUser b=new BankUser();
        expectedBankUsers.add(b);
        TypedQuery<BankUser> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT bu FROM BankUser bu WHERE bu.busr_desg = :designation", BankUser.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("designation", bankUser.getBusr_desg())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedBankUsers);

        List<BankUser> bankUsers = bankUserService.getBankUsersByDesignation(bankUser);

        verify(entityManager).createQuery("SELECT bu FROM BankUser bu WHERE bu.busr_desg = :designation", BankUser.class);
        verify(typedQuery).setParameter("designation", bankUser.getBusr_desg());
        verify(typedQuery).getResultList();
        assert bankUsers == expectedBankUsers;
    }

}
