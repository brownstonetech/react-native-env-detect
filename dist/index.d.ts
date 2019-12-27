export declare type EnvironmentType = 'PRODUCTION' | 'DEV' | 'TESTING';
export default function getEnvAsync(): Promise<EnvironmentType>;
