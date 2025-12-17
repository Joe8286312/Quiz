import React, { useMemo, useState } from 'react';
import { Button, Form, Input, Modal, Select, Space, Switch, Table, Tag, message } from 'antd';

const roleTag = (role) =>
  role === 1 ? <Tag color="red">管理员</Tag> : <Tag color="green">普通用户</Tag>;

const makeMockUsers = () => {
  const list = [];
  for (let i = 1; i <= 23; i++) {
    list.push({
      id: i,
      userName: `user_${i}`,
      userRole: i % 5 === 0 ? 1 : 0,
      createTime: new Date(Date.now() - i * 86400000).toISOString(),
      updateTime: new Date(Date.now() - (i - 1) * 3600000).toISOString(),
    });
  }
  return list;
};

const UsersManager = () => {
  // 数据与状态
  const [data, setData] = useState(makeMockUsers());
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(false);

  // 分页
  const [current, setCurrent] = useState(1);
  const [pageSize] = useState(5);

  // 弹窗
  const [addOpen, setAddOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [adding, setAdding] = useState(false);
  const [editing, setEditing] = useState(false);

  // 表单
  const [addForm] = Form.useForm();
  const [editForm] = Form.useForm();
  const [changePassword, setChangePassword] = useState(false);

  // 过滤与分页数据
  const filtered = useMemo(() => {
    const k = keyword.trim().toLowerCase();
    return k ? data.filter(u => u.userName.toLowerCase().includes(k)) : data;
  }, [data, keyword]);

  const paged = useMemo(() => {
    const start = (current - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, current, pageSize]);

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '用户名', dataIndex: 'userName', width: 200 },
    { title: '用户角色', dataIndex: 'userRole', width: 120, render: roleTag },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 200,
      render: v => new Date(v).toLocaleString('zh-CN'),
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 200,
      render: v => new Date(v).toLocaleString('zh-CN'),
    },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 160,
      render: (_, row) => (
        <Space>
          <a onClick={() => onEdit(row)}>编辑</a>
          <a onClick={() => onDelete(row)}>删除</a>
        </Space>
      ),
    },
  ];

  // 行为
  const onSearch = (value) => {
    setKeyword(value || '');
    setCurrent(1);
  };

  const onReset = () => {
    setKeyword('');
    setCurrent(1);
  };

  const onAdd = () => {
    addForm.resetFields();
    setAddOpen(true);
  };

  const submitAdd = () => {
    addForm.validateFields().then(values => {
      setAdding(true);
      setTimeout(() => {
        const maxId = data.reduce((m, x) => Math.max(m, x.id), 0);
        const now = new Date().toISOString();
        setData([
          {
            id: maxId + 1,
            userName: values.username,
            userRole: values.userRole,
            createTime: now,
            updateTime: now,
          },
          ...data,
        ]);
        setAddOpen(false);
        setAdding(false);
        message.success('添加用户成功');
        setCurrent(1);
      }, 500);
    });
  };

  const onEdit = (row) => {
    editForm.setFieldsValue({
      id: row.id,
      userName: row.userName,
      userRole: row.userRole,
      newPassword: '',
      confirmPassword: '',
    });
    setChangePassword(false);
    setEditOpen(true);
  };

  const submitEdit = () => {
    editForm.validateFields().then(values => {
      setEditing(true);
      setTimeout(() => {
        setData(prev =>
          prev.map(u =>
            u.id === values.id
              ? {
                  ...u,
                  userName: values.userName,
                  userRole: values.userRole,
                  // 仅演示：如果改密就更新更新时间
                  updateTime: new Date().toISOString(),
                }
              : u
          )
        );
        setEditOpen(false);
        setEditing(false);
        message.success('更新用户成功');
      }, 500);
    });
  };

  const onDelete = (row) => {
    Modal.confirm({
      title: '确认删除该用户？',
      content: `ID: ${row.id}，用户名：${row.userName}`,
      okText: '确定',
      cancelText: '取消',
      onOk: () =>
        new Promise((resolve) => {
          setLoading(true);
          setTimeout(() => {
            setData(prev => prev.filter(u => u.id !== row.id));
            setLoading(false);
            message.success('删除成功');
            resolve();
          }, 500);
        }),
    });
  };

  return (
    <>
      <Space wrap style={{ marginBottom: 16 }}>
        <Input.Search
          placeholder="请输入用户名关键词"
          allowClear
          enterButton="查询"
          onSearch={onSearch}
          style={{ width: 320 }}
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <Button onClick={onReset}>重置</Button>
        <Button type="primary" onClick={onAdd}>添加用户</Button>
      </Space>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={paged}
        loading={loading}
        pagination={{
          current,
          pageSize,
          total: filtered.length,
          showSizeChanger: false,
          onChange: (p) => setCurrent(p),
          position: ['bottomLeft'],
          showTotal: (t) => `总计 ${t} 条`,
        }}
        scroll={{ x: 960 }}
      />

      {/* 添加用户 */}
      <Modal
        title="添加用户"
        open={addOpen}
        onOk={submitAdd}
        onCancel={() => setAddOpen(false)}
        okButtonProps={{ loading: adding }}
        destroyOnClose
      >
        <Form form={addForm} layout="vertical">
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            name="password"
            label="密码"
            rules={[{ required: true, message: '请输入密码' }, { min: 6, message: '至少6位' }]}
          >
            <Input.Password placeholder="请输入密码" />
          </Form.Item>
          <Form.Item
            name="confirm"
            label="确认密码"
            dependencies={['password']}
            rules={[
              { required: true, message: '请确认密码' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) return Promise.resolve();
                  return Promise.reject(new Error('两次输入密码不一致'));
                },
              }),
            ]}
          >
            <Input.Password placeholder="请再次输入密码" />
          </Form.Item>
          <Form.Item
            name="userRole"
            label="用户角色"
            initialValue={0}
            rules={[{ required: true, message: '请选择用户角色' }]}
          >
            <Select
              options={[
                { value: 0, label: '普通用户' },
                { value: 1, label: '管理员' },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>

      {/* 编辑用户 */}
      <Modal
        title="编辑用户"
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okButtonProps={{ loading: editing }}
        destroyOnClose
      >
        <Form form={editForm} layout="vertical">
          <Form.Item label="ID" name="id">
            <Input disabled />
          </Form.Item>
          <Form.Item
            name="userName"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            name="userRole"
            label="用户角色"
            rules={[{ required: true, message: '请选择用户角色' }]}
          >
            <Select
              options={[
                { value: 0, label: '普通用户' },
                { value: 1, label: '管理员' },
              ]}
            />
          </Form.Item>

          <Form.Item label="修改密码">
            <Switch
              checked={changePassword}
              checkedChildren="修改密码"
              unCheckedChildren="保持原密码"
              onChange={(v) => {
                setChangePassword(v);
                if (!v) {
                  editForm.setFieldsValue({ newPassword: '', confirmPassword: '' });
                }
              }}
            />
          </Form.Item>
          {changePassword && (
            <>
              <Form.Item
                name="newPassword"
                label="新密码"
                rules={[{ required: true, message: '请输入新密码' }, { min: 6, message: '至少6位' }]}
              >
                <Input.Password placeholder="请输入新密码" />
              </Form.Item>
              <Form.Item
                name="confirmPassword"
                label="确认新密码"
                dependencies={['newPassword']}
                rules={[
                  { required: true, message: '请确认新密码' },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('newPassword') === value) return Promise.resolve();
                      return Promise.reject(new Error('两次输入的新密码不一致'));
                    },
                  }),
                ]}
              >
                <Input.Password placeholder="请再次输入新密码" />
              </Form.Item>
            </>
          )}
        </Form>
      </Modal>
    </>
  );
};

export default UsersManager;