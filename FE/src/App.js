import logo from './logo.svg';
import './App.css';
import {
    BrowserRouter as Router,
    Route,
    Routes as Switch,
    Navigate as Redirect
} from "react-router-dom";
import Login from './Login';
import Administrator from "./Components/Administrator/Administrator";
import Client from "./Components/Client/Client";
import Chart from "./Components/Client/Chart";


function App() {
    console.log(sessionStorage.getItem("role"));
    //const defaultRoute = (window.location.pathname === "/" || (sessionStorage.getItem("role") === false)) ? <Redirect to="/log-in" /> : undefined;

    return (
        <Router>
            <Switch>
                <Route exact path="/log-in" element={<Login />} />
                <Route exact path="/client/:index" element={<Client />} />
                <Route exact path="/admin/:index" element={<Administrator />} />
                <Route exact path="/chart/:index" element={<Chart />} />
                <Route exact path="/" element={<Login />}/>
            </Switch>
            
        </Router>
    );
}

export default App;
