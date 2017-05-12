package net.corda.iou.plugin

import net.corda.core.contracts.Amount
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.crypto.Party
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.node.CordaPluginRegistry
import net.corda.core.node.PluginServiceHub
import net.corda.iou.api.IOUApi
import net.corda.iou.flow.IOUIssueFlow
import net.corda.iou.flow.IOUSettleFlow
import net.corda.iou.flow.IOUTransferFlow
import net.corda.iou.flow.SelfIssueCashFlow
import net.corda.iou.service.IOUService
import net.corda.iou.state.IOUState
import java.util.function.Function

class IOUPlugin : CordaPluginRegistry() {
    /**
     * A list of classes that expose web APIs.
     */
    override val webApis: List<Function<CordaRPCOps, out Any>> = listOf(Function(::IOUApi))

    /**
     * A list of flows required for this CorDapp.
     */
    override val requiredFlows: Map<String, Set<String>> = mapOf(
            IOUIssueFlow::class.java.name to setOf(IOUState::class.java.name, Party::class.java.name),
            IOUTransferFlow::class.java.name to setOf(UniqueIdentifier::class.java.name, Party::class.java.name),
            IOUSettleFlow::class.java.name to setOf(UniqueIdentifier::class.java.name, Amount::class.java.name),
            SelfIssueCashFlow::class.java.name to setOf(Amount::class.java.name)
    )

    /**
     * A list of long-lived services to be hosted within the node.
     */
    override val servicePlugins: List<Function<PluginServiceHub, out Any>> = listOf(Function(IOUService::Service))

    /**
     * A list of directories in the resources directory that will be served by Jetty under /web.
     * The template's web frontend is accessible at /web/template.
     */
    override val staticServeDirs: Map<String, String> = mapOf(
            // This will serve the iouWeb directory in resources to /web/template
            "iou" to javaClass.classLoader.getResource("iouWeb").toExternalForm()
    )
}