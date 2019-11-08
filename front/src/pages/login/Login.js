import React from "react"
import axios from 'axios'
import { Spin, Typography, Form, Icon, Input, Button } from 'antd'

import styles from './Login.module.css'

class Login extends React.PureComponent {
    constructor(props) {
        super(props);
        this.state = {
            login: '',
            password: '',
            loading: false,
            error: null,
            uptime: true,
            errors: {
                login: null,
                password: null
            }
        }
        window.setTimeout(() => {
            this.setState({ uptime: false });
        }, 1000);
    }

    changeLogin = ({ currentTarget }) => {
        const login = currentTarget.value;

        this.setState({ login });
    }

    changePassword = ({ currentTarget }) => {
        const password = currentTarget.value;

        this.setState({ password });
    }

    handleSubmit = (event) => {
        event.preventDefault();

        const { login, password, uptime } = this.state;

        if(uptime) {
            return;
        }

        if(login === '' || password === '') {
            this.setState({ errors: {
                login: !login ? 'Необходимо указать логин' : null,
                password: !password ? 'Необхожимо указать пароль' : null
            }});

            return;
        }

        this.setState({ loading: true });
        axios.post(`${process.env.REACT_APP_API_URL}user/signin`, {
            login,
            password
        }).then((data) => {
            localStorage.setItem('token', data.headers['access-token'])
            setTimeout(() => {
                window.location = '/main';
            }, 100)
        }).catch(error => {
            this.setState({ error: 'Неверный логин или пароль', loading: false });
        })
    }

    render() {
        const { loading, login, password, error , errors } = this.state;

        if (loading) {
            return (
                <div className={styles.content}>
                    <Spin size="large"/>
                </div>
            );
        }

        return (
            <div className={styles.content}>
                <div>
                    <Typography.Text>Данные демо пользователя</Typography.Text><br />
                    <Typography.Text>имя sasha69</Typography.Text><br />
                    <Typography.Text>пароль pass</Typography.Text><br /><br/>

                    <Typography.Text type="danger">{error}</Typography.Text><br />

                    <Form
                        onSubmit={this.handleSubmit}
                        layout="vertical"
                    >
                        <Form.Item
                            label="Введите имя пользователя"
                            validateStatus={ errors.login ? "error" : null}
                            help={errors.login}
                        >
                            <Input
                                name="login"
                                prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                placeholder="Username"
                                onChange={this.changeLogin}
                                value={login}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Введите пароль"
                            validateStatus={ errors.password ? "error" : null}
                            help={errors.password}
                        >
                            <Input
                                name="password"
                                prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                type="password"
                                placeholder="Password"
                                onChange={this.changePassword}
                                value={password}
                            />
                        </Form.Item>
                        <Form.Item>
                            <Button size="large" block type="primary" htmlType="submit">
                                Войти
                            </Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        )
    }
}

export default Login
