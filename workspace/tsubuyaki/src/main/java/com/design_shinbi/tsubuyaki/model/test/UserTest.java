package com.design_shinbi.tsubuyaki.model.test;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.design_shinbi.tsubuyaki.model.dao.UserDAO;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

class UserTest {

	@Test
	void test() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		Connection connection = DbUtil.connect(true);
		UserDAO dao = new UserDAO(connection);

		System.out.println("初期化");
		List<User> users = dao.findAll();
		for (User user : users) {
			dao.delete(user.getId());
		}

		List<User> objects = new ArrayList<User>();
		objects.add(new User("玉井詩織", "shiori@momoclo.jp", "yellow", false));
		objects.add(new User("百田夏菜子", "kanako@momoclo.jp", "red", true));
		objects.add(new User("有安杏果", "momoka@momoclo.jp", "green", false));
		objects.add(new User("佐々木彩夏", "arin@momoclo.jp", "pink", false));
		objects.add(new User("高城れに", "reni@momoclo.jp", "purple", false));

		System.out.println("追加テスト");
		for (User user : objects) {
			dao.add(user.getName(), user.getEmail(), user.getPassword(), user.isAdmin());
		}
		users = dao.findAll();
		if (!same(objects, users)) {
			fail("登録に失敗しました。");
		}
		objects = users;

		System.out.println("更新テスト");
		User momoka = objects.get(2);
		momoka.setName("田中 聡");
		momoka.setEmail("tanaka@momoclo.jp");
		dao.update(momoka);
		users = dao.findAll();
		if (!same(objects, users)) {
			fail("更新に失敗しました。");
		}

		System.out.println("削除テスト");
		dao.delete(momoka.getId());
		objects.remove(2);
		users = dao.findAll();
		if (!same(objects, users)) {
			fail("削除に失敗しました。");
		}

		connection.close();
	}

	public boolean same(List<User> objects, List<User> users)
			throws NoSuchAlgorithmException {
		if (objects.size() != users.size()) {
			return false;
		}

		for (int i = 0; i < objects.size(); i++) {
			User object = objects.get(i);
			User user = users.get(i);
			System.out.println(user);

			if (!object.getName().equals(user.getName())) {
				return false;
			}
			if (!object.getEmail().equals(user.getEmail())) {
				return false;
			}
			if (object.isAdmin() != user.isAdmin()) {
				return false;
			}
		}
		return true;
	}

}
