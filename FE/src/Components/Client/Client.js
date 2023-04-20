import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip, Menu, MenuItem, Paper, Button, Dialog, Typography, DialogTitle, DialogContent, TextField, TextareaAutosize } from '@material-ui/core';
import axiosInstance from "../../axios";
import Header from '../Header/Header';
import MenuIcon from '@material-ui/icons/Menu';
import { useStyles } from './Style.js';
import CloseIcon from '@material-ui/icons/Close';
import Grid from '@material-ui/core/Grid';
import 'date-fns';
import {
    MuiPickersUtilsProvider,
    KeyboardDatePicker,
} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";

const Client = () => {

    const classes = useStyles();
    const [deviceList, setDeviceList] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);
    const [isViewDailyEnergyConsumptionModalOpen, setIsViewDailyEnergyConsumptionModalOpen] = useState(false);
    const [isViewMeasurementsModalOpen, setIsViewMeasurementsModalOpen] = useState(false);
    const [deviceDataset, setDeviceDataset] = useState()
    const [consumptionList, setConsumptionList] = useState([]);
    const [measurementList, setMeasurementList] = useState([]);
    const [isChatModalOpen, setIsChatModalOpen] = useState(false);
    const [message, setMessage] = useState(0);
    const [messageDataset, setMessageDataset] = useState();
    const [messagesString, setMessagesString] = useState("");
    const [someoneIsTyping, setSomeoneIsTyping] = useState(false);

    const dateNow = new Date();

    const [selectedDate, setSelectedDate] = React.useState(dateNow.toISOString());

    const index = useParams();

    function ListItem(props) {
        return <li>{props.value}</li>;
    }

    useEffect(() => getRepo(), [])
    const getRepo = () => {
        console.log(index);
        axiosInstance.get("device/clientDevice/" + index.index)
            .then((res) => {
                const deviceList = res.data;
                console.log(deviceList)
                setDeviceList(deviceList)
                /////////////////////////////////////////
                console.log("In Connect");
                const URL = "http://localhost:8080/socket";
                const websocket = new SockJS(URL);
                const stompClient = Stomp.over(websocket);
                stompClient.connect({}, frame => {
                    var sem = false
                    console.log("Conectat la " + frame);
                    stompClient.subscribe("/topic/socket/client", notification => {
                        for (let i = 0; i < deviceList.length; i++) {
                            console.log(deviceList[i].id)
                            console.log("mesaj:" + notification.body)
                            if (deviceList[i].id === notification.body) {
                                let message = notification.body;
                                console.log("MAXIMUM HOURLY CONSUMPTION EXCEEDED for Device with id: " + message);
                                alert("MAXIMUM HOURLY CONSUMPTION EXCEEDED for Device with id: " + message);
                            }
                        }
                    });

                    stompClient.subscribe("/topic/socket/message/admin", notification => {
                        console.log(localStorage.getItem("receiver"))
                        console.log(sessionStorage.getItem("clientUsername"))
                        if (localStorage.getItem("receiver") == sessionStorage.getItem("clientUsername")) {
                            let message = notification.body;
                            console.log(message);
                            getMessages();
                            //alert(message);
                        }
                    })

                    stompClient.subscribe("/topic/socket/typing/admin", notification => {
                        console.log(localStorage.getItem("receiver"))
                        console.log(sessionStorage.getItem("clientUsername"))
                        if (localStorage.getItem("receiver") == sessionStorage.getItem("clientUsername")) {
                            setSomeoneIsTyping(true);
                            console.log(someoneIsTyping)
                            setTimeout(function () {
                                console.log("aici")
                                setSomeoneIsTyping(false);
                            }.bind(this), 1000);
                            console.log(someoneIsTyping)
                            //alert(notification.body);
                        }
                    })

                    stompClient.subscribe("/topic/socket/read/admin", notification => {
                        console.log(localStorage.getItem("receiver"))
                        console.log(sessionStorage.getItem("clientUsername"))
                        if (localStorage.getItem("receiver") == sessionStorage.getItem("clientUsername")) {
                            let message = notification.body;
                            console.log(message);
                            alert(notification.body);
                        }
                    })
                })
            })
            .catch((err) => {
                console.log(err);
            })
    }

    const viewConsumptionPerHours = () => {
        let credentilas = {
            date: selectedDate.getTime(),
        }
        console.log(selectedDate.getTime())
        console.log(deviceDataset.id)
        axiosInstance.post("consumption/perHours/" + deviceDataset.id, credentilas)
            .then((res) => {
                const consList = res.data;
                setConsumptionList(consList);
                localStorage.setItem("consumptionList", JSON.stringify(res.data));
            })
            .catch((err) => {
                console.log(err);
            })
    }

    const getDeviceConsumption = () => {
        axiosInstance.get("consumption/" + deviceDataset.id)
            .then((res) => {
                const consList = res.data;
                setMeasurementList(consList);
            })
            .catch((err) => {
                console.log(err);
            })
    }

    const generateMeasurements = () => {
        return (
            measurementList ? (
                <TableContainer component={Paper}>
                    <Table className={classes.dialogTable} aria-label="customized table">
                        <TableHead className={classes.tableHead}>
                            <TableRow className={classes.tableRow}>
                                <TableCell className={classes.tableCell}>Timestamp</TableCell>
                                <TableCell className={classes.tableCell}>Energy Consumption</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody className={classes.tableBody}>


                            {measurementList.map((measurement) => (
                                <TableRow className={classes.tableRow} key={measurement.id}>
                                    <TableCell className={classes.tableCell}> {measurement.timestamp}</TableCell>
                                    <TableCell className={classes.tableCell}> {measurement.energyConsumption}</TableCell>
                                </TableRow >
                            ))}


                        </TableBody>
                    </Table>

                </TableContainer>) : (<Typography>No client</Typography>)
        )
    }

    const onMenuClick = (event, device) => {
        event.stopPropagation();
        setDeviceDataset(device);
        setAnchorEl(event.currentTarget);
    };

    const onMenuClose = (e) => {
        e.stopPropagation();
        setAnchorEl(null);
    };

    const onViewConsumption = () => {
        setIsViewDailyEnergyConsumptionModalOpen(true);
        onMenuClose();
    }

    const onViewConsumptionClose = () => {
        setIsViewDailyEnergyConsumptionModalOpen(false);
    }

    const onViewMeasurements = () => {
        getDeviceConsumption();
        setIsViewMeasurementsModalOpen(true);
        onMenuClose();
    }

    const onViewMeasurementsClose = () => {
        setIsViewMeasurementsModalOpen(false);
    }

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const onChat = () => {
        setIsChatModalOpen(true);
        getMessages();
    }

    const onChatClose = () => {
        setIsChatModalOpen(false);
    }

    const onViewChart = () => {
        console.log(selectedDate);
        localStorage.setItem("ClientId", index.index)
        window.location.replace("http://localhost:3000/chart/" + selectedDate.getTime());
        viewConsumptionPerHours();
        onMenuClose();
    }

    const onSend = () => {
        console.log(sessionStorage.getItem("clientUsername"));
        console.log(message);
        console.log(localStorage.getItem("adminUsername"));
        let credentilas = {
            messageSender: sessionStorage.getItem("clientUsername"),
            textMessage: message,
            messageReceiver: localStorage.getItem("adminUsername"),
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
        let credentilas = {
            messageSender: sessionStorage.getItem("clientUsername"),
            messageReceiver: localStorage.getItem("adminUsername"),
        }
        axiosInstance.post("/server/messages", credentilas)
            .then(
                res => {
                    setMessageDataset(res.data);
                    //console.log(String(messageDataset).replaceAll(",", ""));
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
            messageSender: sessionStorage.getItem("clientUsername"),
            messageReceiver: localStorage.getItem("adminUsername"),
        }

        console.log(sessionStorage.getItem("clientUsername"))
        console.log(localStorage.getItem("adminUsername"))

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
            messageSender: sessionStorage.getItem("clientUsername"),
            messageReceiver: localStorage.getItem("adminUsername"),
        }
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
            <Header userType="Client" />
            <div className={classes.buttonContainer}>
                <Button className={classes.addButton} onClick={onChat}>Chat</Button>
            </div>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="customized table">
                    <TableHead className={classes.tableHead}>
                        <TableRow className={classes.tableRow}>
                            <TableCell className={classes.tableCell}>ID</TableCell>
                            <TableCell className={classes.tableCell}>Description</TableCell>
                            <TableCell className={classes.tableCell}>Address</TableCell>
                            <TableCell className={classes.tableCell}>Maximum Hourly Energy Consumption</TableCell>
                            <TableCell className={classes.buttonSpace}></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody className={classes.tableBody}>
                        {deviceList.map((device) => (<>
                            <TableRow className={classes.tableRow} key={device.id}>
                                <TableCell className={classes.tableCell} component="th" scope="row">
                                    {device.id}
                                </TableCell>
                                <TableCell className={classes.tableCell}> {device.description}</TableCell>
                                <TableCell className={classes.tableCell}> {device.address}</TableCell>
                                <TableCell className={classes.tableCell}> {device.maximumHourlyEnergyConsumption}</TableCell>
                                <TableCell className={classes.menuButton}>
                                    <Tooltip title="Options" arrow placement="right">
                                        <Button onClick={(e) => onMenuClick(e, device)}>
                                            <MenuIcon />
                                        </Button>

                                    </Tooltip>
                                </TableCell>
                            </TableRow >

                            <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={onMenuClose}>
                                <MenuItem onClick={onViewConsumption}>Daily Energy Consumption</MenuItem>
                                <MenuItem onClick={onViewMeasurements}>Measurements</MenuItem>
                            </Menu>
                        </>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={isViewMeasurementsModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Consumption</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onViewMeasurementsClose} />
                    </div>

                </DialogTitle>
                <DialogContent >
                    {generateMeasurements()}
                </DialogContent>
            </Dialog>

            <Dialog open={isViewDailyEnergyConsumptionModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">View Daily Energy Consumption</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onViewConsumptionClose} />
                    </div>

                </DialogTitle>
                <DialogContent>
                    <div className={classes.section}>
                        <MuiPickersUtilsProvider utils={DateFnsUtils}>
                            <Grid container justifyContent="space-around">
                                <KeyboardDatePicker
                                    disableToolbar
                                    variant="inline"
                                    format="MM/dd/yyyy"
                                    margin="normal"
                                    id="date-picker-inline"
                                    label="Date picker inline"
                                    value={selectedDate}
                                    onChange={handleDateChange}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />
                            </Grid>
                        </MuiPickersUtilsProvider>
                    </div>
                    <div className={classes.container}>
                        <Button className={classes.button} onClick={onViewChart}>View</Button>
                    </div>
                </DialogContent>
            </Dialog>

            <Dialog open={isChatModalOpen}>
                <DialogTitle style={{ borderBottom: "1px solid lightGrey" }}>
                    <div className={classes.dialogTitle}>
                        <Typography variant="subtitle1">Start a conversation with an administrator</Typography>
                        <CloseIcon style={{ cursor: "pointer" }} onClick={onChatClose} />
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
                                <TextField className={classes.textField} onChange={e => setMessage("C: " + e.target.value + "\n")} onKeyDown={isTyping} label="Compose your message..." />
                                <Button className={classes.markAsReadButton} onClick={viewMessages}>Send</Button>
                                <Button className={classes.addButton} onClick={hasRead}>Mark as read</Button>
                            </div>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    )
}

export default Client;