package com.nkxgen.spring.jdbc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nkxgen.spring.jdbc.Dao.PermissionsDAO;
import com.nkxgen.spring.jdbc.model.Permission;

@Controller

public class PermissionController {

	private final PermissionsDAO permissionsDAO;

	@Autowired
	public PermissionController(PermissionsDAO permissionsDAO) {
		this.permissionsDAO = permissionsDAO;
	}

	@RequestMapping(value = "/permission", method = RequestMethod.GET)
	public String permission(Model model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		// Get the username attribute from the session
		String username = (String) session.getAttribute("username");
		Permission p = permissionsDAO.getPermissions(Long.parseLong(username));
		model.addAttribute("permissions", p);

		return "permission-management";
	}

	// @GetMapping("/permissionurl")
	// public String updatePermissions(@Validated Permission permissions) {
	// System.out.println(permissions.isAccounts());
	// permissionsDAO.updatePermissions(permissions);
	// return "permissions";
	//
	// }
	@RequestMapping(value = "/permissionurl", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> idpermission(Permission permissions) {
		// Assuming customer1 is an instance of your CustomerRepository or service
		System.out.println(permissions);
		permissionsDAO.updatePermissions(permissions);
		return ResponseEntity.ok("Customer data updated successfully");
	}

	// @RequestMapping(value = "/checkUserAccess", method = RequestMethod.POST)
	// public void checkUser(@RequestParam("id") int id, Model model) {
	// System.out.println("called");
	// Permission p = permissionsDAO.getPermissions((long) id);
	// model.addAttribute("permissions", p);
	//
	// }

	@RequestMapping(value = "/allpermissionurl", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> allpermission(Permission permissions) {
		// Assuming customer1 is an instance of your CustomerRepository or service
		System.out.println(permissions);
		permissionsDAO.allUpdatePermissions(permissions);
		return ResponseEntity.ok("Customer data updated successfully");
	}

}