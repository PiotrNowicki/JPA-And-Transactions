import com.piotrnowicki.jpacmt.boundary.BMTMergeBean;
import com.piotrnowicki.jpacmt.boundary.MergeBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.*;

@RunWith(Arquillian.class)
public class BusinessLogicTest {

    @Inject
    MergeBean mergeBean;

    @Inject
    BMTMergeBean bmtMergeBean;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.piotrnowicki.jpacmt").addAsManifestResource("META-INF/persistence.xml", "persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void shouldMergeEntity() {
        mergeBean.mergeEntity();
    }

    @Test
    public void shouldBMTMergeEntity() throws HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {
        bmtMergeBean.mergeEntity();
    }
}
