import {NativeModules} from 'react-native';
export type EnvironmentType = 'PRODUCTION' | 'DEV' | 'TESTING';

const BSTEnvDetect = NativeModules.BSTEnvDetect;
const matchingFingerprint: string = '7b:fa:61:ca:b1:f6:5d:65:5c:3a:61:74:e9:12:26:a5:4c:05:11:4c';

export default async function getEnvAsync(): Promise<EnvironmentType> {
  try {
    if (__DEV__) {
      return 'DEV';
    }
    if (BSTEnvDetect) {
      const ret: EnvironmentType = await BSTEnvDetect.getEnvAsync(matchingFingerprint);
      return ret;
    }
  } catch (e) {
    console.error('Call native module failure', e);
  }
  return 'DEV';
}
