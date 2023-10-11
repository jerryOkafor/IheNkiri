/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 IheNkiri Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.jerryokafor.ihenkiri.tools.lint.composepreview

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

@Suppress("UnstableApiUsage")
class MissingExcludePreviewAnnotationDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UMethod::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return PreviewMethodErrorHandler(context)
    }

    private class PreviewMethodErrorHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitMethod(node: UMethod) {
            val isComposePreviewMethod = node.hasAnnotation(COMPOSE_PREVIEW_ANNOTATION)
            val isThemePreviewMethod = node.hasAnnotation(THEME_PREVIEW_ANNOTATION)
            val hasExcludedFromJacocoReport =
                node.hasAnnotation(EXCLUDE_FROM_JACOCO_REPORT_ANNOTATION)

            if ((isComposePreviewMethod || isThemePreviewMethod) && !hasExcludedFromJacocoReport) {
                context.report(
                    ISSUE,
                    node,
                    context.getLocation(node),
                    ISSUE.getExplanation(
                        TextFormat.TEXT,
                    ),
                )
            }
        }
    }

    companion object {
        private const val COMPOSE_PREVIEW_ANNOTATION = "androidx.compose.ui.tooling.preview.Preview"
        private const val THEME_PREVIEW_ANNOTATION =
            "me.jerryokafor.core.ds.annotation.ThemePreviews"
        private const val EXCLUDE_FROM_JACOCO_REPORT_ANNOTATION =
            "me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport"

        @JvmField
        internal val ISSUE = Issue.create(
            id = "MissingExcludePreviewAnnotationDetector",
            briefDescription = "jetpack compose previews should be excluded from Jacoco report.",
            explanation = "Any method annotated with @Preview or @ThemePreviews should also have @ExcludeFromGeneratedCoverageReport annotation to avoid being added in Jacoco test coverage report.",
            category = Category.CUSTOM_LINT_CHECKS,
            severity = Severity.ERROR,
            implementation = Implementation(
                MissingExcludePreviewAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
            priority = 10,
        )
    }
}
