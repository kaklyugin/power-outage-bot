insert into user_cart (chat_id, city_fias_id, street, normalized_street)
values (1053522116, '28bafcb3-92b2-445b-9443-a341be73fdb9', 'Котовского', 'котовского');

insert into power_outage_notifications (id, user_cart_id, is_notified, notification_text)
values (1, 1, false, 'На ул.Котовского сегодня ожидается отключение света с 9:00 до 10:00');