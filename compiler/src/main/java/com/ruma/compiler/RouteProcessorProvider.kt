package com.ruma.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.ruma.annotations.RouteInfo
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo


private const val ROUTE = "Route"
private const val PATH = "path"
private const val NEEDLOGIN = "needLogin"
private const val ROUTE_PATH = "com.ruma.annotations.Route"
private const val ROUTE_CLASS_NAME = "RouterMap"
private const val ROUTE_PACKAGE_NAME = "com.ruma"

@OptIn(KotlinPoetKspPreview::class)
class RouteProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SimpleProcessor(environment.codeGenerator, environment.logger)
    }

    class SimpleProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

        override fun process(resolver: Resolver): List<KSAnnotated> {
            logger.warn("alex   RouteProcessorProvider process  -- ${System.currentTimeMillis()}")
            val symbols = resolver.getSymbolsWithAnnotation(ROUTE_PATH)
            logger.warn("alex symbols = ${symbols.count()}")
            val elements = symbols.filterIsInstance<KSClassDeclaration>().toList()
            if (elements.isNotEmpty()) {
                try {
                    parseRoute(elements)
                } catch (e: Exception) {
                    logger.exception(e)
                }
            }
            return emptyList()
        }

        private fun parseRoute(elements: List<KSClassDeclaration>) {
            val routeMap = mutableMapOf<String, RouteInfo>()
            val dependencies = mutableSetOf<KSFile>()
            elements.forEach { classDeclaration ->
                val annotation = classDeclaration.annotations.firstOrNull { it.shortName.asString() == ROUTE }
                val path = annotation?.arguments?.firstOrNull { it.name?.asString() == PATH }?.value as? String
                val needLogin = annotation?.arguments?.firstOrNull { it.name?.asString() == NEEDLOGIN }?.value as? Boolean
                if (path != null) {
                    routeMap[path] = RouteInfo(classDeclaration.qualifiedName!!.asString(), needLogin)
                }
                classDeclaration.containingFile?.let { dependencies.add(it) }
            }

            if (routeMap.isNotEmpty()) {
                generateRouterMap(routeMap, dependencies)
            }

        }


        private fun generateRouterMap(routeMap: Map<String, RouteInfo>, dependencies: MutableSet<KSFile>) {
            logger.warn("alex RouteProcessorProvider routeMap.size() = ${routeMap.count()}    -- ${System.currentTimeMillis()}")
            //HashMap<String,Class<?>>
            val mapType = ClassName("java.util", "HashMap").parameterizedBy(String::class.asTypeName(), RouteInfo::class.asClassName())

            val initFunSpec = FunSpec.builder("initRoute").apply {
                routeMap.forEach { (path, routeInfo) ->
                    val sb = StringBuilder()
                    sb.append("map[\"${path}\"").append("] = ").append("RouteInfo(").append("\"${routeInfo.clazz}\"").append(",")
                        .append(routeInfo.needLogin).append(")")
                    addStatement(sb.toString())
                }
            }.build()
            val initFunSpec2 = FunSpec.builder("getRouterMap").addStatement("return map").returns(mapType).build()

            // 使用KotlinPoet构建AutoInject类的属性
            val autoInjectTypeSpec = TypeSpec.objectBuilder(ROUTE_CLASS_NAME).addProperty(
                PropertySpec.builder("map", mapType).mutable(true).initializer("HashMap<String, RouteInfo>()").build()
            ).addFunction(initFunSpec).addFunction(initFunSpec2).build()
            // 将生成的代码写入文件
            val fileSpec = FileSpec.builder(ROUTE_PACKAGE_NAME, ROUTE_CLASS_NAME).addType(autoInjectTypeSpec).build()
            fileSpec.writeTo(codeGenerator, true, dependencies)
        }
    }
}