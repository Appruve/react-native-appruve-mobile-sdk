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
      {userId: '47a025e3-d4a4-4a8f-aa9d-d9dde90d8a15'},
      true, // isGhanaEnabled
      true, // isNigeriaEnabled
      true, // isKenyaEnabled
    )
      .then(response => {
        console.log(`IS_VERIFIED: ${response.isVerified}`);
        console.log(`VERIFICATION_ID ${response.id}`);
        console.log(`ID_PHOTO_URL: ${response.idPhotoUrl}`);
        console.log(`SELFIE_PHOTO_URL: ${response.selfiePhotoUrl}`);
        console.log(`ID_TYPE: ${response.idType}`);
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
