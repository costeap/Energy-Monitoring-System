import { useStyles } from './Style.js';
import { Typography, Avatar } from '@material-ui/core';
import { useNavigate } from 'react-router';
import axiosInstance from "../../axios";

const Header = ({ userType }) => {
    const classes = useStyles();
    const navigation = useNavigate();

    const username = localStorage.getItem("username");
    const parola = localStorage.getItem("parola");

    console.log(username);
    console.log(parola);

    const func = () => {

        let credentilas = {
            username: username,
            parola: parola,
        }

        axiosInstance.post("/login/logout", credentilas)
            .then(
                res => {
                }
            )
            .catch(error => {
                console.log(error)
            })
        window.location.replace("http://localhost:3000/log-in")

    }
    return (
        <div className={classes.header}>
            <Typography variant="h4">{userType}</Typography>
            <Avatar onClick={func} />
        </div>
    )
}

export default Header;