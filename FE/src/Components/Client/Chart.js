import {
    LineChart,
    ResponsiveContainer,
    Legend, Tooltip,
    Line,
    XAxis,
    YAxis,
    CartesianGrid
} from 'recharts';
import { Button } from '@material-ui/core';
import { useNavigate } from 'react-router-dom';

console.log(JSON.parse(localStorage.getItem("consumptionList")))

const App = () => {
    const navigate = useNavigate();

    const redirectToClientPage = () => {
        window.location.assign("/client/" + localStorage.getItem("ClientId"));
    }

    return (
        <>
            <h1 className="text-heading">
                Daily Energy Consumption
            </h1>
            <ResponsiveContainer width="100%" aspect={3}>
                <LineChart data={JSON.parse(localStorage.getItem("consumptionList"))} margin={{ right: 300 }}>
                    <CartesianGrid />
                    <XAxis dataKey="hour"
                        interval={'preserveStartEnd'} />
                    <YAxis></YAxis>
                    <Legend />
                    <Tooltip />
                    <Line dataKey="energyValue"
                        stroke="red" activeDot={{ r: 8 }} />
                </LineChart>
            </ResponsiveContainer>
            <Button variant="contained" color="primary" onClick={redirectToClientPage} >
                Back
            </Button>
        </>
    );
}

export default App;