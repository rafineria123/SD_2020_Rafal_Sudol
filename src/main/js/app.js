'use strict';

const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>

class App extends React.Component { // <1>

    constructor(props) {
        super(props);
        this.state = {employees: []};
    }



    render() { // <3>
        return (
            <div>
                ddd
            </div>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)