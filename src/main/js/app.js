'use strict';
import { Router} from 'react-router';
import routes from './routes';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {employees: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/'}).done(response => {
            this.setState({employees: response.entity._embedded.employees});
        });
    }



    render() {
        return (
            <div>
                <img src="/images/discount_1.jpg" onError="this.src='/images/default_discount_image.png'"
                     className="item_image_discount"></img>
            </div>
        )
    }
}

export default App;

ReactDOM.render(
    <Router routes={routes}/>,
    document.getElementById('react')
);
