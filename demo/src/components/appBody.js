
import React, {Component} from 'react';
import {Platform, Text, View} from 'react-native';
import {Content,Card,CardItem,Body} from 'native-base';
import AppBodyData from './appBodyData';
 
type Props = {};
export default class AppBody extends Component<Props> {


	constructor(){
		super()
		this.state={
			data:[]
		}
	}
	getData(){
		var url = 'https://newsapi.org/v2/top-headlines?' +
          'country=us&' +
          'apiKey=50e2460a44c54f4aba5a5f476ff528a8';
          var req=new Request(url);

		 return fetch(req)
	    .then((response) => response.json())
	    .then((responseJson) => {
	      this.setState({data: responseJson.articles});
	    })
	    .catch((error) => {
	      console.error(error);
	    });

	}
	componentDidMount(){
		this.getData();
	}
 
  render() {
    return (
    		<AppBodyData data = {this.state.data}/>
    );
  }
}
 
module.export = AppBody;