import io.grpc.stub.StreamObserver;
import vms.LuceneSEServiceGrpc;
import vms.Lucenese;

public class LuceneSEServiceImpl extends LuceneSEServiceGrpc.LuceneSEServiceImplBase {
    int count = 0;

    @Override
    public StreamObserver<Lucenese.SomeMessage> searchText(StreamObserver<Lucenese.SomeResult> responseObserver) {
        return new StreamObserver<Lucenese.SomeMessage>() {
            @Override
            public void onNext(Lucenese.SomeMessage value) {
                responseObserver.onNext(
                        Lucenese.SomeResult.newBuilder()
                                .setUniqueid(value.getUniqueid())
                                .setIscontain(count++)
                                .build()
                );
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
