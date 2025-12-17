import React from 'react';
import { Form, Input, Button, Select, Radio } from 'antd';

const { TextArea } = Input;

const formItemLayout = {
  labelCol: { xs: { span: 24 }, sm: { span: 8 } },
  wrapperCol: { xs: { span: 24 }, sm: { span: 16 } },
};
const tailFormItemLayout = {
  wrapperCol: { xs: { span: 24, offset: 0 }, sm: { span: 16, offset: 8 } },
};

const AddQuestion = () => {
  const [form] = Form.useForm();

  const onFinish = (values) => {
    console.log('Received question values: ', values);
  };

  return (
    <Form
      {...formItemLayout}
      form={form}
      name="add-question"
      onFinish={onFinish}
      scrollToFirstError
    >
      <Form.Item
        name="title"
        label="题目内容"
        rules={[{ required: true, message: '请输入题目内容' }]}
      >
        <TextArea rows={3} placeholder="请输入题目内容" />
      </Form.Item>

      <Form.Item
        name="optionA"
        label="选项A"
        rules={[{ required: true, message: '请输入选项A' }]}
      >
        <Input placeholder="选项A" />
      </Form.Item>

      <Form.Item
        name="optionB"
        label="选项B"
        rules={[{ required: true, message: '请输入选项B' }]}
      >
        <Input placeholder="选项B" />
      </Form.Item>

      <Form.Item
        name="optionC"
        label="选项C"
        rules={[{ required: true, message: '请输入选项C' }]}
      >
        <Input placeholder="选项C" />
      </Form.Item>

      <Form.Item
        name="optionD"
        label="选项D"
        rules={[{ required: true, message: '请输入选项D' }]}
      >
        <Input placeholder="选项D" />
      </Form.Item>

      <Form.Item
        name="answer"
        label="正确答案"
        rules={[{ required: true, message: '请选择正确答案' }]}
      >
        <Radio.Group>
          <Radio value="A">A</Radio>
          <Radio value="B">B</Radio>
          <Radio value="C">C</Radio>
          <Radio value="D">D</Radio>
        </Radio.Group>
      </Form.Item>

      {/* <Form.Item
        name="difficulty"
        label="难度"
        rules={[{ required: true, message: '请选择难度' }]}
      >
        <Select
          placeholder="请选择难度"
          options={[
            { value: 'easy', label: '简单' },
            { value: 'medium', label: '中等' },
            { value: 'hard', label: '困难' },
          ]}
        />
      </Form.Item>

      <Form.Item name="explanation" label="答案解析">
        <TextArea rows={3} placeholder="可填写答案解析或备注" />
      </Form.Item> */}

      <Form.Item {...tailFormItemLayout}>
        <Button type="primary" htmlType="submit">
          提交
        </Button>
      </Form.Item>
    </Form>
  );
};

export default AddQuestion;