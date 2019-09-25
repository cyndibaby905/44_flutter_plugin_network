#import "FlutterPluginNetworkPlugin.h"
#import <AFNetworking.h>

@implementation FlutterPluginNetworkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel methodChannelWithName:@"flutter_plugin_network" binaryMessenger:[registrar messenger]];
    FlutterPluginNetworkPlugin* instance = [[FlutterPluginNetworkPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"doRequest" isEqualToString:call.method]) {
        NSDictionary *arguments = call.arguments[@"param"];
        NSString *url = call.arguments[@"url"];
        [self doRequest:url withParams:arguments andResult:result];
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)doRequest:(NSString *)url withParams:(NSDictionary *)params andResult:(FlutterResult)result {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    NSMutableDictionary *newParams = [params mutableCopy];
    newParams[@"ppp"] = @"yyyy";
    [manager GET:url parameters:params progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSString *string = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
        result(string);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        result([FlutterError errorWithCode:@"Error" message:error.localizedDescription details:nil]);
    }];
}
@end

