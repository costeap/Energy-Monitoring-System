import React, { useEffect, useState } from 'react';
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Container from "@material-ui/core/Container";
import axiosInstance from "./axios";
import { Grid } from "@material-ui/core";
import ReCAPTCHA from "react-google-recaptcha";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import { makeStyles } from "@material-ui/core";

const Login = () => {

    const [username, setUsername] = useState(0);
    const [password, setPassword] = useState(0);

    // useEffect(() => {
    //     console.log("In Connect");
    //     const URL = "http://localhost:8080/socket";
    //     const websocket = new SockJS(URL);
    //     const stompClient = Stomp.over(websocket);
    //     stompClient.connect({}, frame => {
    //         console.log("Conectat la " + frame);
    //         stompClient.subscribe("/topic/socket/client", notification => {
    //             let message = notification.body;
    //             console.log(message);
    //             alert(message);

    //         })
    //     })
    // }, []);

    const login = () => {
        let credentilas = {
            username: username,
            password: password,
        }
        console.log(credentilas)

        axiosInstance.post("user/login", credentilas)
            .then(
                res => {
                    let role = false;
                    console.log(res.data);
                    const val = res.status;
                    sessionStorage.setItem("USER_ID", res.data.id);
                    console.log(res.data);
                    if (val == 200) {
                        window.location.assign("/client/" + res.data.id);
                        sessionStorage.setItem("role", false);
                        sessionStorage.setItem("clientUsername", res.data.username);
                    }
                    else if (val == 201) {
                        window.location.assign("/admin/" + res.data.id);
                        sessionStorage.setItem("role", true);
                        localStorage.setItem("adminUsername", res.data.username);
                    }
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const useStyles = makeStyles((theme) => ({
        root: {
            padding: 20,
            margin: "auto",
            boxShadow: "0 2px 12px 1px rgba(60, 62, 66, 0.1)",
            width: "fit-content",
            marginTop: 50

        },
        root1: {
            display: "flex",
            alignItems: "center",
            justifyContent: "space-evenly",
            height: 40,
            padding: "10px 20px",
            margin: 8,
        },
        button: {
            backgroundColor: "#daf1f1"
        },
        container: {
            margin: "auto",
            maxWidth: "fit-content",
            marginTop: 8
        },
        title: {
            margin: "auto",
            width: "fit-content"
        },
        tabs: {
            boxShadow: "5px 0 5px rgb(0 0 0 / 20%)",
        },
        table: {
            margin: "40px auto",
            maxWidth: "70% !important",
        },
        tableUsersActivity: {
            margin: "60px auto",
            maxWidth: "120% !important",
        },
        tableHead: {
            backgroundColor: "#FF8E53"
        },
        tableBody: {
            fontSize: 14
        },
        tableRow: {
            "&:nth-child(even)": {
                backgroundColor: "lightGrey"
            }
        },
        tableCell: {
            maxWidth: 50
        },
        tableCellDialog: {
            maxWidth: 500
        },
        dialogTitle: {
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            minWidth: 500
        },
        dialogTable: {
            margin: "0 auto",
        },
        section: {
            display: "flex",
            justifyContent: "space-around",
            alignItems: "center",
            marginBottom: 10
        },
        addButton: {
            backgroundColor: "#FF8E53"
        },
        buttonContainer: {
            margin: "auto",
            maxWidth: "fit-content",
            marginTop: 20
        },
        container: {
            margin: "auto",
            maxWidth: "fit-content",
            marginTop: 8
        }

    }));

    const classes = useStyles();

    return (
        <Container maxWidth="sm">
            <div>
                <div className={classes.root1}>
                    <TextField variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="string"
                        autoFocus
                        onChange={e => setUsername(e.target.value)} />
                </div>
                <div className={classes.root1}>
                    <TextField variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        onChange={e => setPassword(e.target.value)} />
                </div>
                <div className={classes.container}>
                    <Button className={classes.button} onClick={login}>Sign in</Button>
                </div>
            </div>
        </Container>
    )

}


// class Login extends React.Component {
//     constructor() {
//         super();
//         this.state = {
//             username: "",
//             password: "",
//         };
//         this.state = { items: [], text: '', isVerified: false };
//         this.handleOnChange = this.handleOnChange.bind(this);
//         //this.connect();
//     }

//     handleInput = event => {
//         const { value, name } = event.target;
//         this.setState({
//             [name]: value
//         });
//         console.log(value);
//     };

//     onSubmitFun = event => {
//         event.preventDefault();
//         let credentilas = {
//             username: this.state.username,
//             password: this.state.password,
//         }
//         console.log(credentilas)

//         axiosInstance.post("user/login", credentilas)
//             .then(
//                 res => {
//                     let role = false;
//                     console.log(res.data);
//                     const val = res.status;
//                     sessionStorage.setItem("USER_ID", res.data.id);
//                     console.log(res.data);
//                     if (val == 200) {
//                         window.location.assign("/client/" + res.data.id);
//                         sessionStorage.setItem("role", false);
//                     }
//                     else if (val == 201) {
//                         window.location.assign("/admin/" + res.data.id);
//                         sessionStorage.setItem("role", true);
//                     }
//                 }
//             )
//             .catch(error => {
//                 console.log(error)
//             })
//     }

//     handleOnChange(value) {
//         console.log("Captcha value:", value);
//         this.setState({ isVerified: true })
//     }

//     render() {
//         const { match, location, history } = this.props;
//         return (
//             <Container maxWidth="sm">
//                 <div>
//                     <Grid>
//                         <form onSubmit={this.onSubmitFun}>
//                             <TextField
//                                 variant="outlined"
//                                 margin="normal"
//                                 required
//                                 fullWidth
//                                 id="username"
//                                 label="Username"
//                                 name="username"
//                                 autoComplete="string"
//                                 onChange={this.handleInput}
//                                 autoFocus
//                             />
//                             <TextField
//                                 variant="outlined"
//                                 margin="normal"
//                                 required
//                                 fullWidth
//                                 name="password"
//                                 label="Password"
//                                 type="password"
//                                 id="password"
//                                 onChange={this.handleInput}
//                                 autoComplete="current-password"
//                             />
//                             <ReCAPTCHA
//                                 sitekey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"
//                                 onChange={this.handleOnChange}
//                             />
//                             <Button disabled={!this.state.isVerified}
//                                 type="submit"
//                                 fullWidth
//                                 variant="contained"
//                                 color="primary"
//                             >
//                                 Sign In
//                             </Button>
//                         </form>
//                     </Grid>
//                 </div>
//             </Container>
//         );
//     }

// }

export default Login;