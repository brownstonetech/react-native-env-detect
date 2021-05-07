export declare type EnvironmentType = 'PRODUCTION' | 'DEV' | 'TESTING';
export default function getEnvAsync(uploadFingerprint?: string): Promise<EnvironmentType>;
