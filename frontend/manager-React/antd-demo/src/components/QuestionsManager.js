import React, { useMemo, useState } from 'react';
import { Button, Form, Input, Modal, Radio, Space, Table, message } from 'antd';

const makeMockQuestions = () => {
  const base = [
    { question: '什么是 React？', options: ['一个库', '一个框架', '一门语言', '一个协议'], answer: 'A' },
    { question: 'useState 用于？', options: ['样式', '状态', '路由', '网络'], answer: 'B' },
    { question: 'JSX 是？', options: ['模版语法', '函数式语法', 'HTML 字符串', 'CSS 语法'], answer: 'A' },
  ];
  const list = [];
  for (let i = 1; i <= 17; i++) {
    const b = base[i % base.length];
    // list.push({ id: i, question: `${b.question} #${i}`, options: [...b.options], answer: b.answer });
    // 移除 “#${i}” 即可去掉每条题目后的序号标记
    list.push({ id: i, question: b.question, options: [...b.options], answer: b.answer });
  }
  return list;
};

const QuestionsManager = () => {
  const [data, setData] = useState(makeMockQuestions());
  const [keyword, setKeyword] = useState('');
  const [loading, setLoading] = useState(false);

  const [current, setCurrent] = useState(1);
  const [pageSize] = useState(5);

  const [addOpen, setAddOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [adding, setAdding] = useState(false);
  const [editing, setEditing] = useState(false);

  const [addForm] = Form.useForm();
  const [editForm] = Form.useForm();

  const filtered = useMemo(() => {
    const k = keyword.trim();
    return k ? data.filter(q => q.question.includes(k)) : data;
  }, [data, keyword]);

  const paged = useMemo(() => {
    const start = (current - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, current, pageSize]);

  const columns = [
    { title: '序号', dataIndex: 'id', width: 80 },
    { title: '题目', dataIndex: 'question', width: 320 },
    {
      title: '选项',
      dataIndex: 'options',
      width: 360,
      render: (opts = []) =>
        opts.map((o, i) => `${String.fromCharCode(65 + i)}. ${o}`).join('  |  '),
    },
    { title: '答案', dataIndex: 'answer', width: 100 },
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
    addForm.validateFields().then(v => {
      setAdding(true);
      setTimeout(() => {
        const nextId = data.reduce((m, x) => Math.max(m, x.id), 0) + 1;
        setData([{ id: nextId, question: v.title, options: [v.optionA, v.optionB, v.optionC, v.optionD], answer: v.answer }, ...data]);
        setAddOpen(false);
        setAdding(false);
        message.success('添加题目成功');
        setCurrent(1);
      }, 500);
    });
  };

  const onEdit = (row) => {
    editForm.setFieldsValue({
      id: row.id,
      title: row.question,
      optionA: row.options?.[0],
      optionB: row.options?.[1],
      optionC: row.options?.[2],
      optionD: row.options?.[3],
      answer: row.answer,
    });
    setEditOpen(true);
  };

  const submitEdit = () => {
    editForm.validateFields().then(v => {
      setEditing(true);
      setTimeout(() => {
        setData(prev =>
          prev.map(q =>
            q.id === v.id
              ? { ...q, question: v.title, options: [v.optionA, v.optionB, v.optionC, v.optionD], answer: v.answer }
              : q
          )
        );
        setEditOpen(false);
        setEditing(false);
        message.success('更新题目成功');
      }, 500);
    });
  };

  const onDelete = (row) => {
    Modal.confirm({
      title: '确认删除该题目？',
      content: `ID: ${row.id}，题目：${row.question}`,
      okText: '确定',
      cancelText: '取消',
      onOk: () =>
        new Promise((resolve) => {
          setLoading(true);
          setTimeout(() => {
            setData(prev => prev.filter(q => q.id !== row.id));
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
          placeholder="请输入题目关键词"
          allowClear
          enterButton="查询"
          onSearch={onSearch}
          style={{ width: 320 }}
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <Button onClick={onReset}>重置</Button>
        <Button type="primary" onClick={onAdd}>添加题目</Button>
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

      {/* 添加题目 */}
      <Modal
        title="添加题目"
        open={addOpen}
        onOk={submitAdd}
        onCancel={() => setAddOpen(false)}
        okButtonProps={{ loading: adding }}
        destroyOnClose
      >
        <Form layout="vertical" form={addForm}>
          <Form.Item name="title" label="题目内容" rules={[{ required: true, message: '请输入题目内容' }]}>
            <Input.TextArea rows={3} placeholder="请输入题目内容" />
          </Form.Item>
          <Form.Item name="optionA" label="选项A" rules={[{ required: true, message: '请输入选项A' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionB" label="选项B" rules={[{ required: true, message: '请输入选项B' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionC" label="选项C" rules={[{ required: true, message: '请输入选项C' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionD" label="选项D" rules={[{ required: true, message: '请输入选项D' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="answer" label="正确答案" rules={[{ required: true, message: '请选择正确答案' }]}>
            <Radio.Group>
              <Radio value="A">A</Radio>
              <Radio value="B">B</Radio>
              <Radio value="C">C</Radio>
              <Radio value="D">D</Radio>
            </Radio.Group>
          </Form.Item>
        </Form>
      </Modal>

      {/* 编辑题目 */}
      <Modal
        title="编辑题目"
        open={editOpen}
        onOk={submitEdit}
        onCancel={() => setEditOpen(false)}
        okButtonProps={{ loading: editing }}
        destroyOnClose
      >
        <Form layout="vertical" form={editForm}>
          <Form.Item name="id" label="ID">
            <Input disabled />
          </Form.Item>
          <Form.Item name="title" label="题目内容" rules={[{ required: true, message: '请输入题目内容' }]}>
            <Input.TextArea rows={3} placeholder="请输入题目内容" />
          </Form.Item>
          <Form.Item name="optionA" label="选项A" rules={[{ required: true, message: '请输入选项A' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionB" label="选项B" rules={[{ required: true, message: '请输入选项B' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionC" label="选项C" rules={[{ required: true, message: '请输入选项C' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="optionD" label="选项D" rules={[{ required: true, message: '请输入选项D' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="answer" label="正确答案" rules={[{ required: true, message: '请选择正确答案' }]}>
            <Radio.Group>
              <Radio value="A">A</Radio>
              <Radio value="B">B</Radio>
              <Radio value="C">C</Radio>
              <Radio value="D">D</Radio>
            </Radio.Group>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default QuestionsManager;