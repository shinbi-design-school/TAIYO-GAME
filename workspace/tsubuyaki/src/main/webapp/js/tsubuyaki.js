function newUser() {
    $('#operation_input').val('new');
    $('#user_form').submit();
}

function editUser(id) {
    $('#operation_input').val('edit');
    $('#user_id_input').val(id);
    $('#user_form').submit();
}

function deleteUser(id) {
    const ret = confirm('ユーザーを削除してもよろしいですか?');
    if(ret) {
        $('#operation_input').val('delete');
        $('#user_id_input').val(id);
        $('#user_form').submit();
    }
}

function toggleNewMessage() {
    const currentStatus = $('#new_message_area').css('display');
    if(currentStatus == 'none') {
        $('#new_message_area').css('display', 'block');
        $('#new_message_icon').removeClass('fa-plus-square');
        $('#new_message_icon').addClass('fa-minus-square');
    }
    else {
        $('#new_message_area').css('display', 'none');
        $('#new_message_icon').removeClass('fa-minus-square');
        $('#new_message_icon').addClass('fa-plus-square');
    }
}

function removeImage() {
    $('#message_image_file').css('display', 'inline');
    $('#image_file_name').css('display', 'none');
    $('#delete_image_input').val('true');
    $('#message_image_file').val('');
}

function editMessage(id) {
    $('#id_input').val(id);
    $('#operation_input').val('edit');
    $('#message_operation_form').submit();
}

function deleteMessage(id) {
    const ret = confirm('メッセージを削除してもよろしいですか?');
    if(ret) {
        $('#id_input').val(id);
        $('#operation_input').val('delete');
        $('#message_operation_form').submit();
    }
}

