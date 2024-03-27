package com.design_shinbi.tsubuyaki.model.test;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.design_shinbi.tsubuyaki.model.dao.MessageDAO;
import com.design_shinbi.tsubuyaki.model.dao.UserDAO;
import com.design_shinbi.tsubuyaki.model.entity.Message;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

class MessageTest {

	@Test
	void test() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		Connection connection = DbUtil.connect(true);
		UserDAO userDao = new UserDAO(connection);
		MessageDAO dao = new MessageDAO(connection);

		List<User> users = userDao.findAll();
		if (users.isEmpty()) {
			fail("UserTest を先に実行してください。");
		}

		List<Message> messages = dao.findAll();
		for (Message message : messages) {
			dao.delete(message.getId());
		}

		String[] array = {
				"Yes! Yes! We're the ももいろクローバー",
				"レニ カナコ シオリ アヤカ モモカ",
				"Go! Now! 君のハート めがけて Sing a Song!",
				"チャイムが鳴ったら 急いで集合! 宿題なんかは している暇ない"
		};

		List<Message> objects = new ArrayList<Message>();
		for (int i = 0; i < array.length; i++) {
			int index = i % users.size();
			User user = users.get(index);
			objects.add(new Message(user.getId(), array[i]));
		}

		System.out.println("追加テスト");
		for (Message message : objects) {
			dao.add(message.getUserId(), message.getText());
		}
		messages = dao.findAll();
		if (!same(objects, messages)) {
			fail("登録に失敗しました。");
		}
		objects = messages;

		System.out.println("更新テスト");
		Message message = objects.get(1);
		message.setText("レニ カナコ シオリ ササキアヤカ");
		dao.update(message);
		messages = dao.findAll();
		if (!same(objects, messages)) {
			fail("更新に失敗しました。");
		}

		System.out.println("削除テスト");
		message = objects.get(3);
		dao.delete(message.getId());
		objects.remove(3);
		messages = dao.findAll();
		if (!same(objects, messages)) {
			fail("削除に失敗しました。");
		}

		connection.close();
	}

	public boolean same(List<Message> objects, List<Message> messages) {
		if (objects.size() != messages.size()) {
			return false;
		}

		for (int i = 0; i < objects.size(); i++) {
			Message object = objects.get(i);
			Message message = messages.get(i);
			System.out.println(message);

			if (object.getUserId() != message.getUserId()) {
				return false;
			}
			if (!object.getText().equals(message.getText())) {
				return false;
			}
		}
		return true;
	}
}
