import React, { useState, useEffect } from 'react';
import { useStyles } from '../Style.js';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip, Menu, MenuItem, Paper, Button, Dialog, DialogTitle, DialogContent, TextField, Typography, TextareaAutosize } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import CloseIcon from '@material-ui/icons/Close';
import axiosInstance from "../../../axios";
import { Alert, AlertTitle } from '@material-ui/lab';
import WebSocketListenerInstance from '../../Ws/WebSocketListener.js';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";


const ClientPage = ({ clientList }) => {

    const classes = useStyles();

    const [anchorEl, setAnchorEl] = useState(null);
    const [isAddModalOpen, setIsAddModalOpen] = useState();
    const [isAddAdminModalOpen, setIsAddAdminModalOpen] = useState();
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isChatModalOpen, setIsChatModalOpen] = useState(false);
    const [clientDataset, setClientDataset] = useState();
    const [messageDataset, setMessageDataset] = useState();
    const [someoneIsTyping, setSomeoneIsTyping] = useState(false);

    //for insert client
    const [nameClient, setNameClient] = useState(0);
    const [usernameClient, setUsernameClient] = useState(0);
    const [passwordClient, setPasswordClient] = useState(0);

    //for update client
    const [nameClientUpdate, setNameClientUpdate] = useState(0);
    const [passwordClientUpdate, setPasswordClientUpdate] = useState(0);

    //for insert admin
    const [nameAdmin, setNameAdmin] = useState(0);
    const [usernameAdmin, setUsernameAdmin] = useState(0);
    const [passwordAdmin, setPasswordAdmin] = useState(0);

    const [message, setMessage] = useState(0);

    const onMenuClick = (event, user) => {
        event.stopPropagation();
        setClientDataset(user);
        sessionStorage.setItem("currentMessages", clientDataset.username);
        setAnchorEl(event.currentTarget);
    };

    const onMenuClose = (e) => {
        e.stopPropagation();
        setAnchorEl(null);
    };

    const onAddClient = () => {
        setIsAddModalOpen(true);
    }

    const onAddClientClose = () => {
        setIsAddModalOpen(false);
    }

    const onAddAdmin = () => {
        setIsAddAdminModalOpen(true);
    }

    const onAddAdminClose = () => {
        setIsAddAdminModalOpen(false);
    }

    const onEditClient = () => {
        setIsEditModalOpen(true);
        onMenuClose();
    }

    const onEditClose = () => {
        setIsEditModalOpen(false);
    }

    const onChatWithClient = () => {
        setIsChatModalOpen(true);
        getMessages();
    }

    const onChatWithClientClose = () => {
        setIsChatModalOpen(false);
    }

    useEffect(() => {
        console.log("In Connect");
        const URL = "http://localhost:8080/socket";
        const websocket = new SockJS(URL);
        const stompClient = Stomp.over(websocket);
        stompClient.connect({}, frame => {
            console.log("Conectat la " + frame);
            stompClient.subscribe("/topic/socket/message/client", notification => {
                let message = notification.body;
                console.log(message);
                getMessages();
                //alert(message);

            })

            stompClient.subscribe("/topic/socket/typing/client", notification => {
                console.log("aici " + notification.body);
                console.log("aici " + sessionStorage.getItem("currentMessages"))
                if (notification.body === sessionStorage.getItem("currentMessages")) {
                    setSomeoneIsTyping(true);
                    console.log(someoneIsTyping)
                    setTimeout(function () {
                        setSomeoneIsTyping(false);
                    }.bind(this), 1000);
                    console.log(someoneIsTyping);
                }
            })

            stompClient.subscribe("/topic/socket/read/client", notification => {
                if (notification.body === sessionStorage.getItem("currentMessages")) {
                    let message = notification.body;
                    console.log(message);
                    alert(notification.body + " has read your messages");
                }
            })
        })
    }, []);

    const onDeleteClient = () => {
        console.log(clientDataset)
        axiosInstance.delete("/client/del/" + clientDataset.id)
            .then(
                res => {

                    <Alert severity="success">
                        <AlertTitle>Success</AlertTitle>
                        Client deleted
                    </Alert>
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const editClient = () => {
        let credentilas = {
            name: nameClientUpdate,
            password: passwordClientUpdate,
        }

        axiosInstance.put("/client/" + clientDataset.id, credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const addNewClient = () => {
        let credentilas = {
            name: nameClient,
            username: usernameClient,
            password: passwordClient,
        }

        axiosInstance.post("/client", credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const addNewAdmin = () => {
        let credentilas = {
            name: nameAdmin,
            username: usernameAdmin,
            password: passwordAdmin,
        }

        axiosInstance.post("/admin", credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
    }

    const onSend = () => {
        console.log(clientDataset.username);
        console.log(message);
        console.log(localStorage.getItem("adminUsername"));
        localStorage.setItem("receiver", clientDataset.username);
        let credentilas = {
            messageSender: localStorage.getItem("adminUsername"),
            textMessage: message,
            messageReceiver: clientDataset.username,
        }
        axiosInstance.post("/server", credentilas)
            .then(
                res => {
                    console.log("sal" + res);
                }
            ).catch((err) => {
                console.log(err);
            })
    }

    const getMessages = () => {
        //setAux(1);
        let credentilas = {
            messageSender: localStorage.getItem("adminUsername"),
            //messageReceiver: clientDataset.username,
            messageReceiver: sessionStorage.getItem("currentMessages"),
        }
        axiosInstance.post("/server/messages", credentilas)
            .then(
                res => {
                    setMessageDataset(res.data);
                    //console.log(String(messageDataset).replaceAll(",",""));
                    //setMessageDataset(String(res.data).replaceAll(",",""))
                }
            ).catch((err) => {
                console.log(err);
            })
    }

    const viewMessages = () => {
        onSend();
        getMessages();
    }

    const isTyping = () => {
        let credentilas = {
            messageSender: localStorage.getItem("adminUsername"),
            messageReceiver: sessionStorage.getItem("currentMessages"),
        }

        localStorage.setItem("receiver", clientDataset.username);
        console.log(localStorage.getItem("adminUsername"))
        console.log(sessionStorage.getItem("currentMessages"))

        axiosInstance.post("/server/typing", credentilas)
            .then(
                res => {
                }
            ).catch((err) => {
                console.log(err);
            })
    }

    const hasRead = () => {
        let credentilas = {
            messageSender: localStorage.getItem("adminUsername"),
            messageReceiver: sessionStorage.getItem("currentMessages"),
        }

        localStorage.setItem("receiver", clientDataset.username);
        axiosInstance.post("/server/read", credentilas)
            .then(
                res => {
                }
            ).catch((err) => {
                console.log(err);
            })
    }

    return (
        <div>
            <div className={classes.buttonContainer}>
                <Button className={classes.addButton} onClick={onAddClient}>Add new client</Button>
                <Button className={classes.addButton} onClick={onAddAdmin}>Add new administrator</Button>
            </div>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="customized table">
                    <TableHead className={classes.tableHead}>
                        <TableRow className={classes.tableRow}>
                            <TableCell className={classes.tableCell}>ID</TableCell>
                            <TableCell className={classes.tableCell}>Name</TableCell>
                            <TableCell className={classes.tableCell}>Username</TableCell>
                            <TableCell className={classes.buttonSpace}></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody className={classes.tableBody}>
                        {clientList.map((client) => (<>
                            <TableRow className={classes.tableRow} key={client.id}>
                                <TableCell className={classes.tableCell} component="th" scope="row">
                                    {client.id}
                                </TableCell>
                                <TableCell className={classes.tableCell}> {client.name}</TableCell>
                                <TableCell className={classes.tableCell}> {client.username}</TableCell>
                                <TableCell className={classes.menuButton}>
                                    <Tooltip title="Options" arrow placement="right">
                                        <Button onClick={(e) => onMenuClick(e, client)}>
                                            <MenuIcon />
                                        </Button>

                                    </Tooltip>
                                </TableCell>
                            </TableRow >

                            <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={onMenuClose}>
                                <MenuItem onClick={onDeleteClient}>Delete Client</MenuItem>
                                <MenuItem onClick={onEditClient} >Edit Client</MenuItem>
                                <MenuItem onClick={onChatWithClient}>Chat</MenuItem>
                            </Menu>
                        </>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {clientDataset &&
                <Dialog open={isEditModalOpen}>
                    <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                        <div className={classes.dialogTitle}>
                            <Typography variant="subtitle1">Edit</Typography>
                            <CloseIcon style={{ cursor: "pointer" }} onClick={onEditClose} />
                        </div>

                    </DialogTitle>
                    <DialogContent>
                        <div className={classes.section}>
                            <TextField onChange={e => setNameClientUpdate(e.target.value)} label="Name" variant="filled" />
                            <TextField onChange={e => setPasswordClientUpdate(e.target.value)} label="Password" variant="filled" />
                        </div>
                        <div className={classes.container}>
                            <Button className={classes.button} onClick={editClient}>Save</Button>
                        </div>
                    </DialogContent>
                </Dialog>

            }

            <Dialog open={isAddModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Add Client</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onAddClientClose} />
                    </div>

                </DialogTitle>
                <DialogContent >
                    <div className={classes.section}>
                        <TextField onChange={e => setNameClient(e.target.value)} label="Name" variant="filled" />
                        <TextField onChange={e => setUsernameClient(e.target.value)} label="Username" variant="filled" />
                    </div>
                    <div className={classes.section}>
                        <TextField onChange={e => setPasswordClient(e.target.value)} label="Password" variant="filled" />
                    </div>
                    <div className={classes.container}>
                        <Button className={classes.button} onClick={addNewClient}>Add</Button>
                    </div>
                </DialogContent>
            </Dialog>

            <Dialog open={isAddAdminModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Add Administrator</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onAddAdminClose} />
                    </div>

                </DialogTitle>
                <DialogContent >
                    <div className={classes.section}>
                        <TextField onChange={e => setNameAdmin(e.target.value)} label="Name" variant="filled" />
                        <TextField onChange={e => setUsernameAdmin(e.target.value)} label="Username" variant="filled" />
                    </div>
                    <div className={classes.section}>
                        <TextField onChange={e => setPasswordAdmin(e.target.value)} label="Password" variant="filled" />
                    </div>
                    <div className={classes.container}>
                        <Button className={classes.button} onClick={addNewAdmin}>Add</Button>
                    </div>
                </DialogContent>
            </Dialog>

            {clientDataset &&
                <Dialog open={isChatModalOpen}>
                    <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                        <div className={classes.dialogTitle}>
                            <Typography variant="subtitle1">{clientDataset.name}</Typography>
                            <CloseIcon style={{ cursor: "pointer" }} onClick={onChatWithClientClose} />
                        </div>

                    </DialogTitle>
                    <DialogContent >
                        <div className={classes.dialogTitle}>
                            <div className={classes.container}>
                                <div className={classes.container}>
                                    <TextareaAutosize className={classes.textArea}
                                        maxRows={4}
                                        aria-label="maximum height"
                                        placeholder="Maximum 4 rows"
                                        defaultValue={messageDataset}
                                    />
                                </div>
                                {someoneIsTyping == true ? <span>Typing...</span> : <span></span>
                                }
                                <div className={classes.section}>
                                    <TextField className={classes.textField} onChange={e => setMessage("A: " + e.target.value + "\n")} onKeyDown={isTyping} label="Compose your message..." />
                                    <Button className={classes.markAsReadButton} onClick={viewMessages}>Send</Button>
                                    <Button className={classes.addButton} onClick={hasRead}>Mark as read</Button>
                                </div>
                            </div>
                        </div>
                    </DialogContent>
                </Dialog>
            }

        </div>
    )

}

export default ClientPage;