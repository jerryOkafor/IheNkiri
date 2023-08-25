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
@file:Suppress("TooManyFunctions", "MatchingDeclarationName")

package me.jerryokafor.core.ds.theme

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.jerryokafor.core.common.annotation.ExcludeFromJacocoGeneratedReport

data class IheNkiriSpacing(
    val none: Dp = 0.dp,
    val quarter: Dp = 2.dp,
    val half: Dp = 4.dp,
    val one: Dp = 8.dp,
    val oneAndHalf: Dp = 12.dp,
    val two: Dp = 16.dp,
    val twoAndaHalf: Dp = 20.dp,
    val three: Dp = 24.dp,
    val four: Dp = 32.dp,
    val five: Dp = 40.dp,
    val fiveAndHalf: Dp = 44.dp,
    val six: Dp = 48.dp,
    val seven: Dp = 56.dp,
    val eight: Dp = 64.dp,
    val nine: Dp = 72.dp,
    val ten: Dp = 80.dp,
    val twelve: Dp = 96.dp,
)

@Composable
@ExcludeFromJacocoGeneratedReport
fun QuarterVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.quarter))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun HalfVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.half))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun OneVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.one))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun OneAndHalfVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.oneAndHalf))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TwoVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.two))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TwoAndHalfVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.twoAndaHalf))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun ThreeVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.three))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun FourVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.four))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun FiveVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.five))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun SixVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.six))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun EightVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.eight))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TenVerticaSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.ten))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TwelveVerticalSpacer() {
    Spacer(modifier = Modifier.height(IheNkiri.spacing.twelve))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun ColumnScope.FillingSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}

// Horizontal Spacers
@Composable
@ExcludeFromJacocoGeneratedReport
fun QuarterHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.quarter))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun HalfHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.half))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun OneHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.one))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun OneAndHalfHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TwoHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.two))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun ThreeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.three))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun TwoAndHalfHorizontalSpacer() {
    Spacer(modifier = Modifier.width(IheNkiri.spacing.twoAndaHalf))
}

@Composable
@ExcludeFromJacocoGeneratedReport
fun RowScope.FillingSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}

val LocalIheNkiriSpacing = staticCompositionLocalOf { IheNkiriSpacing() }
