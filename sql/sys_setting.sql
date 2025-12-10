create table sys_setting
(
    setting_component_name varchar(100)    not null comment '设置组件名称'
        primary key,
    setting_name           varchar(100)    null comment '设置名称',
    setting_json           varbinary(2000) null comment '设置参数'
)
    comment '用户和登陆后设置关联表' row_format = DYNAMIC;

INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('GrayModelSetting', '灰色模式', 0x7B22656E61626C65223A66616C73657D);
INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('IntervalUpdatePasswordSetting', '定期修改密码', 0x7B22656E61626C65223A66616C73652C22756E6974223A226D6F6E7468227D);
INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('RestrictAccessIpSetting', '限制访问IP', 0x7B22656E61626C65223A66616C73652C2269704C697374223A5B22225D7D);
INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('SameAccountLoginSetting', '同账号登录限制', 0x7B22656E61626C65223A66616C73652C226D6178696D756D223A317D);
INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('SignInSetting', '自助注册', 0x7B22656E61626C65223A747275652C2264657074496473223A5B2231383130323236323034373930363537303235222C2231383432313238343039363030393137353035225D2C2264656661756C74446570744964223A6E756C6C2C22706F7374496473223A5B2231383432313838303839333932303431393836225D2C22726F6C65496473223A5B2231383432313439383531303637353134383831225D7D);
INSERT INTO shanzhu_restarts.sys_setting (setting_component_name, setting_name, setting_json) VALUES ('VerificationCodeSetting', '验证码', 0x7B22656E61626C65223A66616C73657D);
