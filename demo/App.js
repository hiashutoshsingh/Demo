/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, Text, View} from 'react-native';
import AppHeader from './src/components/appHeader';
import AppFooter from './src/components/appFooter';
import AppBody from './src/components/appBody';
import {Container,StyleProvider} from 'native-base';
import getTheme from './src/themes/components';
import ashuTheme from './src/themes/variables/lessonTheme';


type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <StyleProvider style={getTheme(ashuTheme)}>
      <Container> 
          <AppHeader/>
          <AppBody/>
          <AppFooter/> 
      </Container>
      </StyleProvider>
    );
  }
}

