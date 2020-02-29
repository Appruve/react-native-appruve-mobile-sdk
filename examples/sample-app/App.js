/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Button, StyleSheet, View} from 'react-native';
import AppruveMobileSdk from 'react-native-appruve-mobile-sdk';

export default class ButtonBasics extends Component {
  _onPressButton() {
    AppruveMobileSdk.startVerification(
      'YOUR API TOKEN',
      '#00693C',
      '#0F5738',
      '#292f33',
      '#292f33',
      '#00693C',
      '#292f33',
      '#75AF96',
      '#292f33',
      '#75AF96',
      true,
      true,
      true,
    )
      .then(r => {
        console.log(r);
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.buttonContainer}>
          <Button onPress={this._onPressButton} title="Start Verification" />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  buttonContainer: {
    margin: 20,
  },
  alternativeLayoutButtonContainer: {
    margin: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});
